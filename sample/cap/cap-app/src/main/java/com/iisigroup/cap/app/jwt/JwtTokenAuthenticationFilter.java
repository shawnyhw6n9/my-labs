/* 
 * JwtTokenAuthenticationFilter.java
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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;

import com.iisigroup.cap.app.utils.JwtTokenUtil;

/**
 * <pre>
 * JWT Token Authentication Filter
 * </pre>
 * 
 * @since Aug 22, 2019
 * @author Sunkist Wang
 * @version
 *          <ul>
 *          <li>Aug 22, 2019,Sunkist Wang,new
 *          </ul>
 */
public class JwtTokenAuthenticationFilter extends GenericFilterBean {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private JwtTokenUtil jwtTokenProvider;

    private UserDetailsService userDetailsService;

    public JwtTokenAuthenticationFilter(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {

        String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(jwtTokenProvider.getUsername(token));
                Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (BadCredentialsException e) {
            log.info("Jwt authentication failed: {}", e.getMessage());

            ((HttpServletResponse) res).setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        filterChain.doFilter(req, res);
    }

}