package com.urbfix.api.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("UrbFix API")
                        .version("1.0")
                        .description("API para o sistema de gestão de problemas urbanos UrbFix"));
    }
}
