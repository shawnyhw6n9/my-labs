/* 
 * SecurityConfig.java
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

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.iisigroup.cap.app.jwt.JwtAuthenticationEntryPoint;
import com.iisigroup.cap.app.jwt.JwtTokenAuthenticationFilter;
import com.iisigroup.cap.app.utils.JwtTokenUtil;

/**
 * <pre>
 * Web Security Configuration.
 * </pre>
 * 
 * @since Aug 5, 2019
 * @author Sunkist Wang
 * @version
 *          <ul>
 *          <li>Aug 5, 2019,Sunkist Wang,new
 *          </ul>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private JwtTokenUtil jwtTokenProvider;

    @Value("${jwt.route.authentication.path:#{null}}")
    private String authPath;

    @Value("${jwt.route.authentication.refresh:#{null}}")
    private String refreshPath;

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.authorizeRequests()
        .antMatchers("/actuator/hystrix.stream").permitAll()
        .antMatchers("/turbine.stream").permitAll()
        .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/webjars/springfox-swagger-ui/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/doc/**").permitAll()
                .antMatchers("/" + authPath).permitAll()
                .antMatchers("/" + refreshPath).permitAll()
                .anyRequest().authenticated().and().httpBasic().and().csrf().disable();
        http.antMatcher("/**").exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint()).and().addFilterAfter(authenticationTokenFilterBean(),
                UsernamePasswordAuthenticationFilter.class);
        // @formatter:on
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(this.dataSource);
        super.configure(auth);
    }

    // @Bean
    // public JdbcUserDetailsManager jdbcUserDetailsManager(
    // SecurityProperties properties,
    // ObjectProvider<PasswordEncoder> passwordEncoder) {
    // SecurityProperties.User user = properties.getUser();
    // List<String> roles = user.getRoles();
    // return new InMemoryUserDetailsManager(User.withUsername(user.getName())
    // .password(getOrDeducePassword(user, passwordEncoder.getIfAvailable()))
    // .roles(StringUtils.toStringArray(roles)).build());
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
        manager.setDataSource(dataSource);
        // Only for initiating empty database
//         manager.createUser(User.withUsername("admin").password(passwordEncoder().encode("123456")).authorities("ADMIN").build());
//         manager.createUser(User.withUsername("user").password(passwordEncoder().encode("123456")).authorities("USER").build());
        return manager;
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder()); // 在Auth物件上設定加密元件
        return authProvider;
    }

    @Bean
    public JwtTokenAuthenticationFilter authenticationTokenFilterBean() throws Exception {
        return new JwtTokenAuthenticationFilter(userDetailsService(), jwtTokenProvider);
    }

    // @Bean
    // public AuthenticationFailureHandler customAuthenticationFailureHandler() {
    // return new CustomAuthenticationFailureHandler();
    // }
}
