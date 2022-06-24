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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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

            IntStream.range(0, 0);
            
            
            Assert.assertNull("queryByDeviceAndId", javaUtil.queryByDeviceAndId(mongoDatabase, null, null));
            Assert.assertNull("queryByDeviceAndId", javaUtil.queryByDeviceAndId(mongoCollection, null, null));
            
            List<String> result = new ArrayList<String>();
            result.add(javaUtil.queryByDeviceAndId(mongoCollection, "D1", null));
            result.add(javaUtil.queryByDeviceAndId(mongoCollection, "D1", "A123"));
            result.add(javaUtil.queryByDeviceAndId(mongoCollection, "D2", ""));
            result.add(javaUtil.queryByDeviceAndId(mongoCollection, "", "A456"));
            result.add(javaUtil.queryByDeviceAndId(mongoCollection, "D2", "A456"));
            result.add(javaUtil.queryByDeviceAndId(mongoCollection, "D3", "A789"));
            result.add(javaUtil.queryByDeviceAndId(mongoCollection, "D3", "A246"));
            result.add(javaUtil.queryByDeviceAndId(mongoCollection, "D4", ""));
            result.add(javaUtil.queryByDeviceAndId(mongoCollection, "D5", ""));
            result.add(javaUtil.queryByDeviceAndId(mongoCollection, "D4", "A135"));
            result.add(javaUtil.queryByDeviceAndId(mongoCollection, "D5", "A135"));
            result.add(javaUtil.queryByDeviceAndId(mongoCollection, "D5", "A222"));
            result.add(javaUtil.queryByDeviceAndId(mongoCollection, "D5", "A444"));

            result.stream().forEach(r-> System.out.println(r));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
