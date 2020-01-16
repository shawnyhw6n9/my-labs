/* 
 * AuthServiceImpl.java
 * 
 * Copyright (c) 2019 International Integrated System, Inc. 
 * All Rights Reserved.
 * 
 * Licensed Materials - Property of International Integrated System, Inc.
 * 
 * This software is confidential and proprietary information of 
 * International Integrated System, Inc. (&quot;Confidential Information&quot;).
 */
package com.iisigroup.cap.app.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.iisigroup.cap.app.service.AuthService;
import com.iisigroup.cap.app.utils.JwtTokenUtil;

/**
 * <pre>
 * Auth Service for login and refresh token.
 * </pre>
 * 
 * @since Aug 22, 2019
 * @author Sunkist Wang
 * @version
 *          <ul>
 *          <li>Aug 22, 2019,Sunkist Wang,new
 *          </ul>
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtTokenUtil jwtTokenProvider;

    @Value("${jwt.tokenHead:#{null}}")
    private String tokenHead;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String rawPassword) {
        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (rawPassword != null && rawPassword.length() > 0 && passwordEncoder.matches(rawPassword, userDetails.getPassword())) {
            final String token = jwtTokenProvider.generateToken(userDetails);
            return token;
        }
        return null;
    }

    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.contains(tokenHead) ? oldToken.substring(tokenHead.length()) : oldToken;
        String username = jwtTokenProvider.getUsernameFromToken(token);
        UserDetails user = (UserDetails) userDetailsService.loadUserByUsername(username);
        if (jwtTokenProvider.canTokenBeRefreshed(token, (Date) jwtTokenProvider.getCreatedDateFromToken(token))) {
            return jwtTokenProvider.refreshToken(token);
        }
        return null;
    }

}
