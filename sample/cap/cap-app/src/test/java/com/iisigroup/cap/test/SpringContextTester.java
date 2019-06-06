/* 
 * SpringContextTester.java
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.iisigroup.cap.app.CapApplication;
import com.iisigroup.cap.app.controller.SampleController;
import com.iisigroup.cap.utils.CapAppContext;

/**
 * <pre>
 * Test Spring Context
 * </pre>
 * 
 * @since Jun 6, 2019
 * @author Sunkist Wang
 * @version
 *          <ul>
 *          <li>Jun 6, 2019,Sunkist Wang,new
 *          </ul>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CapApplication.class)
public class SpringContextTester {

    @Test
    public void test() {
        SampleController controller = CapAppContext.getBean("SampleController");
        Assert.assertNull("Assert controller is not null", controller);
    }

}
