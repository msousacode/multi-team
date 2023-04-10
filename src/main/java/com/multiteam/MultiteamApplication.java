package com.multiteam;

import com.multiteam.config.AppPropertiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppPropertiesConfig.class)
@SpringBootApplication
public class MultiteamApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiteamApplication.class, args);
	}

}
