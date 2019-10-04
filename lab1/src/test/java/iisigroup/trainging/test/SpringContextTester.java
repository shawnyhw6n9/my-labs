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
package iisigroup.trainging.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import iisigroup.trainging.app.CapApplication;
import iisigroup.trainging.app.controller.SampleController;

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
public class SpringContextTester implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Test
    public void test() {
        String beanName = "SampleController";
        SampleController controller = applicationContext.containsBean(beanName) ? (SampleController) applicationContext.getBean(beanName) : null;
        Assert.assertNull("Assert controller is not null", controller);
    }

}
