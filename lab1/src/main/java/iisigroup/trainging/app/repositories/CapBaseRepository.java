/* 
 * CapBaseRepository.java
 * 
 * Copyright (c) 2019 International Integrated System, Inc. 
 * All Rights Reserved.
 * 
 * Licensed Materials - Property of International Integrated System, Inc.
 * 
 * This software is confidential and proprietary information of 
 * International Integrated System, Inc. (&quot;Confidential Information&quot;).
 */
package iisigroup.trainging.app.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * <pre>
 * Base Repository
 * </pre>
 * 
 * @since Jun 11, 2019
 * @author Sunkist Wang
 * @version
 *          <ul>
 *          <li>Jun 11, 2019,Sunkist Wang,new
 *          </ul>
 */
@NoRepositoryBean
public interface CapBaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, QuerydslPredicateExecutor<T> {

}
