/* 
 * CustomerManagementImpl.java
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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iisigroup.cap.app.model.Customer;
import com.iisigroup.cap.app.repositories.CustomerRepository;
import com.iisigroup.cap.app.service.CustomerManagement;

/**
 * <pre>
 * Customer Service
 * </pre>
 * 
 * @since Jun 10, 2019
 * @author Sunkist Wang
 * @version
 *          <ul>
 *          <li>Jun 10, 2019,Sunkist Wang,new
 *          </ul>
 */
@Service
public class CustomerManagementImpl implements CustomerManagement {
    @Autowired
    CustomerRepository customerRepository;

    @Transactional
    public String updateCustomerInfos(String lastName) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Customer> customers = new ArrayList<Customer>();
        customerRepository.save(new Customer("Sunkist", "Wang"));
        customerRepository.save(new Customer("Albert", "Einstein"));
        customers = customerRepository.findByLastName("Wang");
        for (Customer customer : customers) {
            customer.setLastName("Einstein");
            stringBuilder.append(customer.toString()).append("${symbol_es}"+ "n");
        }
        return stringBuilder.toString();
    }
}
