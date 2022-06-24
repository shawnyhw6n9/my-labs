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

import org.junit.Assert;
import org.junit.Test;

import com.mongodb.client.MongoCollection;
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
        MongoCollection mongoCollection = mongoDatabase.getCollection(collection);

        try {

            JavaUtil javaUtil = new JavaUtil();

            Assert.assertNull("queryByDeviceAndId", javaUtil.queryByDeviceAndId(null, null, null));
            Assert.assertNull("queryByDeviceAndId", javaUtil.queryByDeviceAndId(mongoCollection, null, null));
            Assert.assertNotNull("queryByDeviceAndId", javaUtil.queryByDeviceAndId(mongoCollection, "D1", null));
            Assert.assertNotNull("queryByDeviceAndId", javaUtil.queryByDeviceAndId(mongoCollection, "D1", "A123"));
            Assert.assertNotNull("queryByDeviceAndId", javaUtil.queryByDeviceAndId(mongoCollection, "D2", ""));
            Assert.assertNotNull("queryByDeviceAndId", javaUtil.queryByDeviceAndId(mongoCollection, "", "A456"));
            Assert.assertNotNull("queryByDeviceAndId", javaUtil.queryByDeviceAndId(mongoCollection, "D2", "A456"));
            Assert.assertNotNull("queryByDeviceAndId", javaUtil.queryByDeviceAndId(mongoCollection, "D3", "A789"));
            Assert.assertNotNull("queryByDeviceAndId", javaUtil.queryByDeviceAndId(mongoCollection, "D3", "A246"));
            Assert.assertNotNull("queryByDeviceAndId", javaUtil.queryByDeviceAndId(mongoCollection, "D4", ""));
            Assert.assertNotNull("queryByDeviceAndId", javaUtil.queryByDeviceAndId(mongoCollection, "D5", ""));
            Assert.assertNotNull("queryByDeviceAndId", javaUtil.queryByDeviceAndId(mongoCollection, "D4", "A135"));
            Assert.assertNotNull("queryByDeviceAndId", javaUtil.queryByDeviceAndId(mongoCollection, "D5", "A135"));
            Assert.assertNotNull("queryByDeviceAndId", javaUtil.queryByDeviceAndId(mongoCollection, "D5", "A222"));
            Assert.assertNotNull("queryByDeviceAndId", javaUtil.queryByDeviceAndId(mongoCollection, "D5", "A444"));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
