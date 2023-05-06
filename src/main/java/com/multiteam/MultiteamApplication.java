package com.multiteam;

import com.multiteam.core.configuration.AppPropertiesConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppPropertiesConfiguration.class)
@SpringBootApplication
public class MultiteamApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiteamApplication.class, args);
	}

}
