/* 
 * AuthController.java
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

import javax.annotation.Resource;
import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iisigroup.cap.app.service.AuthService;
import com.iisigroup.cap.app.utils.JwtTokenUtil;

/**
 * <pre>
 * Auth Controller for JWT
 * </pre>
 * 
 * @since Aug 22, 2019
 * @author Sunkist Wang
 * @version
 *          <ul>
 *          <li>Aug 22, 2019,Sunkist Wang,new
 *          </ul>
 */
@RestController
public class AuthController {

    @Value("${jwt.header:#{null}}")
    private String tokenHeader;

    @Resource
    private AuthService authService;

    @Resource
    private JwtTokenUtil jwtTokenProvider;

    @Resource
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "${jwt.route.authentication.path:#{null}}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestParam String user, @RequestParam String password) throws AuthenticationException {
        final String token = authService.login(user, password);
        // Return the token
        if (token == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(token);
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh:#{null}}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) throws AuthenticationException {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if (refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(refreshedToken);
        }
    }
}
