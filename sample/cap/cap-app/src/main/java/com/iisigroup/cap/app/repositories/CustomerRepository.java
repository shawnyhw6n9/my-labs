/* 
 * CustomerRepository.java
 * 
 * Copyright (c) 2019 International Integrated System, Inc. 
 * All Rights Reserved.
 * 
 * Licensed Materials - Property of International Integrated System, Inc.
 * 
 * This software is confidential and proprietary information of 
 * International Integrated System, Inc. (&quot;Confidential Information&quot;).
 */
package com.iisigroup.cap.app.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iisigroup.cap.app.model.Customer;

/**
 * <pre>
 * Customer Repository
 * </pre>
 * 
 * @since Jun 10, 2019
 * @author Sunkist Wang
 * @version
 *          <ul>
 *          <li>Jun 10, 2019,Sunkist Wang,new
 *          </ul>
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {
    List<Customer> findByLastName(String lastName);
}
