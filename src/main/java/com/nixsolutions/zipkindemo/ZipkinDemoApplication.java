package com.nixsolutions.zipkindemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ZipkinDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZipkinDemoApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI().info(apiInfo());
	}

	private Info apiInfo() {
		Info info = new Info();
		info
				.title("Zipkin Demo Application")
				.version("2.1")
				.description("Service description");
		return info;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
