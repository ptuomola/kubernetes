package org.tuomola.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Project {

	public static void main(String[] args) {
		SpringApplication.run(Project.class, args);
	}

	@Configuration
	public class StaticResourceConfiguration implements WebMvcConfigurer {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
					registry.addResourceHandler("/images/**").addResourceLocations("file:/var/tmp/app/files/");
			}
	}
}