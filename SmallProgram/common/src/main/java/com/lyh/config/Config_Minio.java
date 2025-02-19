package com.lyh.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * minio文件存储配置
 */
@Configuration
public class Config_Minio {

    @Value(value = "${minio.host}")
    private String host;

    @Value(value = "${minio.accessKey}")
    private String accessKey;

    @Value(value = "${minio.secretKey}")
    private String secretKey;

    @Bean
    MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(host)
                .credentials(accessKey,secretKey)
                .build();
    }
}