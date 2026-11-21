package com.projeto.emprestimo.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI retornarAPI() {
        OpenAPI openAPI = new OpenAPI();
        Info info = new Info()
                .title("API de Empréstimo de Equipamentos")
                .description("API REST para controlar o empréstimo de equipamentos " +
                        "(notebooks, monitores e headsets) " +
                        "a colaboradores: cadastro, empréstimos, devoluções e acompanhamento de atrasos.");

        return openAPI.info(info);
    }

}
