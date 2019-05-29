/* 
 * TracingServer.java
 * 
 * Copyright (c) 2019 International Integrated System, Inc. 
 * All Rights Reserved.
 * 
 * Licensed Materials - Property of International Integrated System, Inc.
 * 
 * This software is confidential and proprietary information of 
 * International Integrated System, Inc. (&quot;Confidential Information&quot;).
 */
package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import zipkin2.server.internal.EnableZipkinServer;

/**
 * <pre>
 * For open tracing by enable zipkin.
 * </pre>
 * 
 * @since May 29, 2019
 * @author Sunkist Wang
 * @version
 *          <ul>
 *          <li>May 29, 2019,Sunkist Wang,new
 *          </ul>
 */
@SpringBootApplication
@EnableZipkinServer
public class TracingServer {

    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(TracingServer.class, args);
    }

}
