package com.rehabilitation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j配置类
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("运动康复医疗管理平台API")
                        .version("1.0.0")
                        .description("运动康复医疗管理平台后端接口文档")
                        .contact(new Contact()
                                .name("康复平台开发团队")
                                .email("admin@rehabilitation.com")));
    }
}
