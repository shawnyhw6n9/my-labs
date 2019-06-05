package com.iisigroup.cap.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * <pre>
 * Config server.
 * </pre>
 * 
 * @since Apr 29, 2019
 * @author Sunkist Wang
 * @version
 *          <ul>
 *          <li>Apr 29, 2019,Sunkist Wang,new
 *          </ul>
 */
@EnableEurekaServer
@EnableDiscoveryClient
@EnableConfigServer
@SpringBootApplication
public class CapCloudConfigServerApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CapCloudConfigServerApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CapCloudConfigServerApplication.class, args);
    }

}