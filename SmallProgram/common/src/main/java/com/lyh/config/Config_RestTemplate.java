package com.lyh.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;

@Configuration
public class Config_RestTemplate {

    @Bean
    public RestTemplate restTemplate() {
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate restTemplate = new RestTemplate(factory);
        // 添加自定义的拦截器
        restTemplate.setInterceptors(Collections.singletonList(new CustomRequestResponseLoggingInterceptor()));
        return restTemplate;
    }

    private static class CustomRequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {
        @NotNull
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, @NotNull ClientHttpRequestExecution execution) throws IOException {
            if (body.length > 0) {
                System.out.println("Request Body: " + new String(body, Charset.defaultCharset()));
            }
            ClientHttpResponse response = execution.execute(request, body);
//            String responseBody = StreamUtils.copyToString(response.getBody(), Charset.defaultCharset());
            return response;
        }
    }
}
