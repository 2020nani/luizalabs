package com.luizalabs_desafio.infrastructure.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Desafio LuizaLabs")
                        .version("1.0")
                        .description("Documentação da API criada para gerenciar pedidos conforme proposto" +
                                " pelo desafio tecnico luizalabs"))
                .servers(List.of(new Server().url("/luizalabs/v1")));
    }
}

