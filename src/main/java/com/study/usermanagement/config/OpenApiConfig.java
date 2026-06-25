package com.study.usermanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("个人财务管理系统接口文档")
                        .version("1.0")
                        .description("基于 Spring Boot + MyBatis + MySQL 的个人收支管理系统接口文档"));
    }

    @Bean
    public GroupedOpenApi userManagementApi() {
        return GroupedOpenApi.builder()
                .group("user-management")
                .packagesToScan("com.study.usermanagement.controller")
                .build();
    }
}
