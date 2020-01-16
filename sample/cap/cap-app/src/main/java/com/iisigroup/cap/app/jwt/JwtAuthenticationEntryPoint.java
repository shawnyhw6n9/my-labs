/* 
 * JwtAuthenticationEntryPoint.java
 * 
 * Copyright (c) 2019 International Integrated System, Inc. 
 * All Rights Reserved.
 * 
 * Licensed Materials - Property of International Integrated System, Inc.
 * 
 * This software is confidential and proprietary information of 
 * International Integrated System, Inc. (&quot;Confidential Information&quot;).
 */
package com.iisigroup.cap.app.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * <pre>
 * JWT Authentication Entry Point
 * </pre>
 * 
 * @since Aug 22, 2019
 * @author Sunkist Wang
 * @version
 *          <ul>
 *          <li>Aug 22, 2019,Sunkist Wang,new
 *          </ul>
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    Logger log = LoggerFactory.getLogger(this.getClass());

    /***/
    public JwtAuthenticationEntryPoint() {
    }

    /* (non-Javadoc)
     * @see org.springframework.security.web.AuthenticationEntryPoint#commence(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("Jwt authentication failed: {}", authException.getMessage());

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Jwt authentication failed");
    }

}
