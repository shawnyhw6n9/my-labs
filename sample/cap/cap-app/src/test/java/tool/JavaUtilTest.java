/* 
 * JavaUtilTest.java
 * 
 * Copyright (c) 2022 International Integrated System, Inc. 
 * All Rights Reserved.
 * 
 * Licensed Materials - Property of International Integrated System, Inc.
 * 
 * This software is confidential and proprietary information of 
 * International Integrated System, Inc. (&quot;Confidential Information&quot;).
 */
package tool;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mongodb.client.MongoDatabase;

/**
 * <pre>
 * TODO Write a short description on the purpose of the program
 * </pre>
 * 
 * @since 2022年6月24日
 * @author 1104300
 * @version
 *          <ul>
 *          <li>2022年6月24日,1104300,new
 *          </ul>
 */
public class JavaUtilTest {

    @Test
    public void test() {

        MongoBean bean = new MongoBean();
        MongoDatabase mongoDatabase = bean.mongoDatabase();
        String collection = bean.getCollection();
        
        String result = null;
        try {
            result = new JavaUtil().queryByDeviceAndId(mongoDatabase.getCollection(collection), "D1", null);
            System.out.printf("RESULT => %s", result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        assertNotNull(result);
    }

}
