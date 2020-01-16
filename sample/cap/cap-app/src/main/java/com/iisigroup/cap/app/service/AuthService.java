/* 
 * AuthService.java
 * 
 * Copyright (c) 2019 International Integrated System, Inc. 
 * All Rights Reserved.
 * 
 * Licensed Materials - Property of International Integrated System, Inc.
 * 
 * This software is confidential and proprietary information of 
 * International Integrated System, Inc. (&quot;Confidential Information&quot;).
 */
package com.iisigroup.cap.app.service;

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
public interface AuthService {

    String login(String username, String rawPassword);

    String refresh(String oldToken);
}
