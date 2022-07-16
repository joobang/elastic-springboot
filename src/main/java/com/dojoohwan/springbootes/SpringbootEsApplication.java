package com.dojoohwan.springbootes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class SpringbootEsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootEsApplication.class, args);
	}

}
