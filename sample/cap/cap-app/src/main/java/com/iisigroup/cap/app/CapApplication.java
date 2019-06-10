package com.iisigroup.cap.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.iisigroup.cap.utils.CapAppContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * <pre>
 * Spring Boot Application.
 * </pre>
 * 
 * @since May 2, 2019
 * @author Sunkist Wang
 * @version
 *          <ul>
 *          <li>May 2, 2019,Sunkist Wang,new
 *          </ul>
 */
@EnableDiscoveryClient
@EnableJpaRepositories("com.iisigroup.cap.app.repositories")
@SpringBootApplication
public class CapApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CapApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CapApplication.class, args);
    }

    @Bean
    public CapAppContext capAppContext() {
        return new CapAppContext();
    }
}