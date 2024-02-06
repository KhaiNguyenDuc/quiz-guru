package com.khai.quizguru;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication

public class QuizGuruApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizGuruApplication.class, args);
	}

	@Value("${openai.api.key}")
	public String apiKey;

	@Value("${dictionary.api.key}")
	private String dictionaryAPIKey;

	@Value("${dictionary.api.host}")
	private String dictionaryHost;
	@Bean
	public RestTemplate template(){
		RestTemplate restTemplate=new RestTemplate();
		restTemplate.getInterceptors().add((request, body, execution) -> {
			request.getHeaders().add("Authorization", "Bearer " + apiKey);
			request.getHeaders().add("X-RapidAPI-Key", dictionaryAPIKey);
			request.getHeaders().add("X-RapidAPI-Host", dictionaryHost);
			return execution.execute(request, body);
		});
		return restTemplate;
	}
		@Bean
		public WebMvcConfigurer configure() {
			return new WebMvcConfigurer() {
				@Override
				public void addCorsMappings(CorsRegistry registry) {
					registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE")
							.allowedOrigins("*")
							.allowedHeaders("*");
				}
			};
		}


}
