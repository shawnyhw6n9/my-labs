package com.iisigroup.cap.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

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
@SpringBootApplication
public class CapApplication {

    public static void main(String[] args) {
        SpringApplication.run(CapApplication.class, args);
    }

}