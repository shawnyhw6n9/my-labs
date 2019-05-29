/* 
 * SwaggerConfig.java
 * 
 * Copyright (c) 2019 International Integrated System, Inc. 
 * All Rights Reserved.
 * 
 * Licensed Materials - Property of International Integrated System, Inc.
 * 
 * This software is confidential and proprietary information of 
 * International Integrated System, Inc. (&quot;Confidential Information&quot;).
 */
package com.iisigroup.cap.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * <pre>
 * Swagger Config
 * </pre>
 * 
 * @since Jan 29, 2019
 * @author Sunkist Wang
 * @version
 *          <ul>
 *          <li>Jan 29, 2019,Sunkist Wang,new
 *          </ul>
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {

    @Value("${info.app.version: x.x.x}")
    private String apiVersion;

    @Bean
    public Docket createIntegrationApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build();

    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfoBuilder().title("com.iisigroup.cap.app.CAP").description("Cap by Spring Boot Application").version(apiVersion).build();
        return apiInfo;
    }
}
