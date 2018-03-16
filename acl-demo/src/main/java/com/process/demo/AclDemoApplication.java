package com.process.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.process.demo.config.MethodSecurityConfiguration;
import com.process.demo.config.SecurityConfiguration;
import com.process.demo.config.WebMvcConfiguration;

@SpringBootApplication
@ComponentScan
@EnableJpaRepositories
@EntityScan
public class AclDemoApplication {

	public static void main(String[] args) {
//		SpringApplication.run(AclDemoApplication.class, args);
		SpringApplication.run(new Class[] { 
				AclDemoApplication.class, 
				SecurityConfiguration.class, 
				WebMvcConfiguration.class, 
				MethodSecurityConfiguration.class }, args);
	    
	}
}
