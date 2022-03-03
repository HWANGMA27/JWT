package com.tutorial.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class JwtApplication {

    private static final String APPLICATION =
            "spring.config.location=" +
                    "classpath:/application.yml," +
                    "classpath:/application_auth.yml";
    public static void main(String[] args) {
        new SpringApplicationBuilder(JwtApplication.class).properties(APPLICATION).run(args);
    }

}
