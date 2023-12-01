package br.com.erudio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
	
	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("Docker from Zero to Mastery - Demystified Containerization!")
				.version("v1")
				.description("Some description about your API")
				.termsOfService("https://pub.erudio.com.br/meus-cursos")
				.license(
					new License()
						.name("Apache 2.0")
						.url("https://pub.erudio.com.br/meus-cursos")
					)
				);
	}

}
