package org.tuomola.project;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Project {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Project.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
		app.run(args);
	}

	@Configuration
	public class StaticResourceConfiguration implements WebMvcConfigurer {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
					registry.addResourceHandler("/images/**").addResourceLocations("file:/var/tmp/app/files/");
			}
	}
}