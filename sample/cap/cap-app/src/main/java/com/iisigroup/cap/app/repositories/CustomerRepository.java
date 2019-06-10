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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
// @Transactional(readOnly = true)
public interface CustomerRepository extends JpaRepository<Customer, String> {
    // @Modifying
    // @Transactional(timeout = 10)
    @Query("select c from Customer c where c.lastName = ?1")
    List<Customer> findByLastName(String lastName);
}
