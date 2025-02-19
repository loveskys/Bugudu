package com.lyh;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

/**
 * 小程序后台
 */
//@EnableCaching
@EnableAspectJAutoProxy
@SpringBootApplication
public class SpApplication {

    private static final Logger logger = LoggerFactory.getLogger(SpApplication.class);

    @Resource
    private Environment env;

    public static void main(String[] args) {
        final PrintStream originalOut = System.out;
        final PrintStream originalErr = System.err;
        System.setOut(new PrintStream(System.out) {
            @Override
            public void println(String x) {
                logger.info(x);
                originalOut.println(x);
            }
        });
        System.setErr(new PrintStream(System.err) {
            @Override
            public void println(String x) {
                logger.error(x);
                originalErr.println(x);
            }
        });
        SpringApplication.run(SpApplication.class, args);
    }

    //
    @Bean
    public ApplicationRunner configPrinter() {
        return args -> {
            String[] activeProfiles = this.env.getActiveProfiles();
            System.out.println("启动成功，使用的配置：" + String.join(",", activeProfiles));
        };
    }
}

