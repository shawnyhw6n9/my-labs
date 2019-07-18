/* 
 * RedisClusterTester.java
 * 
 * Copyright (c) 2019 International Integrated System, Inc. 
 * All Rights Reserved.
 * 
 * Licensed Materials - Property of International Integrated System, Inc.
 * 
 * This software is confidential and proprietary information of 
 * International Integrated System, Inc. (&quot;Confidential Information&quot;).
 */
package com.iisigroup.cap.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.iisigroup.cap.app.CapApplication;

/**
 * <pre>
 * After start redis cluster , Test get value from Redis
 * </pre>
 * 
 * @since Jun 6, 2019
 * @author Sunkist Wang
 * @see cap-app/src/main/resources/redis-cluster/README.md
 * @version
 *          <ul>
 *          <li>Jun 6, 2019,Sunkist Wang,new
 *          </ul>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CapApplication.class)
public class RedisClusterTester {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void getValue() {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        Assert.assertEquals("Equals", "value1", operations.get("key1"));
    }

}
