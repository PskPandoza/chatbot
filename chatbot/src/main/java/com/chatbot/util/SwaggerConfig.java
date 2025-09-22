package com.chatbot.util;

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
	                .title("Whatsapp API")
	                .version("1.0")
	                .description("API documentation for Diwise_In_Blogs_And_Contacts"))
	            .servers(List.of(
//	                new Server().url("https://tomcat.diwise.in/DiwiseInAdminPanel/"),
	                new Server().url("http://localhost:8080/")
	            ));
	    }
}
