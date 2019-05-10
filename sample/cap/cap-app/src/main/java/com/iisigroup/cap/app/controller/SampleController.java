/* 
 * TestController.java
 * 
 * Copyright (c) 2019 International Integrated System, Inc. 
 * All Rights Reserved.
 * 
 * Licensed Materials - Property of International Integrated System, Inc.
 * 
 * This software is confidential and proprietary information of 
 * International Integrated System, Inc. (&quot;Confidential Information&quot;).
 */
package com.iisigroup.cap.app.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 * Sample Controller.
 * </pre>
 * 
 * @since May 2, 2019
 * @author Sunkist Wang
 * @version
 *          <ul>
 *          <li>May 2, 2019,Sunkist Wang,new
 *          </ul>
 */
@RestController
@RefreshScope
public class SampleController {

    @Value("${upload.path:}")
    private String uploadPath;

    @Value("${db.jdbc.url:}")
    private String dbJdbcUrl;

    @RequestMapping("/configs")
    public String showConfig() {
        return "uploadPath= " + this.uploadPath + ",dbJdbcUrl= " + this.dbJdbcUrl;
    }
}
