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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * <pre>
 * Tester of JavaUtil
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

    private static Logger log = LoggerFactory.getLogger(JavaUtilTest.class);

    public static String MY_URI = "mongodb://poc_mega:hk4g4rufu4@ip-172-31-4-180.ap-southeast-1.compute.internal:27078,ip-172-31-12-79.ap-southeast-1.compute.internal:27078,ip-172-31-4-180.ap-southeast-1.compute.internal:27078/?replicaSet=tag";

    public static String getMyUri() {
        return MY_URI;
    }
    
    @Test
    public void test() {

        MongoBean bean = new MongoBean();
        bean.setUri(JavaUtilTest.getMyUri());
        String collection = bean.getCollection();
        String database = bean.getDatabase();
        MongoClient mongoClient = bean.mongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        MongoCollection mongoCollection = mongoDatabase.getCollection(collection);
        
        Date sDate = new Date();
        System.out.printf("================================== Start time => %s\n\n", sDate);
        
        try {

            JavaUtil javaUtil = new JavaUtil();

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

            mongoClient.close();

        } catch (Exception e) {
            // TODOed Auto-generated catch block
            e.printStackTrace();
        }
        
        Date eDate = new Date();
        System.out.printf("==================================   End time => %s, %d in milliseconds\n\n", eDate, (eDate.getTime() - sDate.getTime()));
    }

    @Test
    public void delAll() {
        MongoBean bean = new MongoBean();
        bean.setUri(JavaUtilTest.getMyUri());
        String collection = bean.getCollection();
        String database = bean.getDatabase();
        MongoClient mongoClient = bean.mongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        MongoCollection mongoCollection = mongoDatabase.getCollection(collection);

        // FIXME
//        mongoCollection.deleteMany(Filters.or(Filters.in("DeviceId", "D1"), Filters.in("DeviceId", "D2"), Filters.in("DeviceId", "D3"), Filters.in("DeviceId", "D4"), Filters.in("DeviceId", "D5"),
//                Filters.in("Id", "A456")));
        // FIXME drop all!
        mongoCollection.drop();

        mongoClient.close();
    }

    @Test
    public void testFile1m() {
    
        MongoBean bean = new MongoBean();
        bean.setUri(JavaUtilTest.getMyUri());
        String collection = bean.getCollection();
        String database = bean.getDatabase();
        MongoClient mongoClient = bean.mongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        MongoCollection mongoCollection = mongoDatabase.getCollection(collection);
    
        Date sDate = new Date();
        System.out.printf("================================== Start time => %s\n\n", sDate);
    
        String filename = FilenameEnum.F1m.getCode();
        List<String[]> inputDataList = parseFile(filename);
        // FIMXE no print
        // inputDataList.stream().forEach(r -> System.out.printf("Coulmn 0= %s, Column 1=%s\n", r[0], r[1]));
    
        Date fDate = new Date();
        System.out.printf("==================================   Parse File Process time => %d in milliseconds\n\n", (fDate.getTime() - sDate.getTime()));
    
        List<String> result = new ArrayList<String>();
    
        // FIXME
        // by loop...
        // for (String[] i: inputDataList) {
        // result.add(javaUtil.queryByDeviceAndId(mongoCollection, i[0], i[1]));
        // }
        inputDataList.stream().forEach(i -> {
            try {
                result.add(JavaUtil.queryByDeviceAndId(mongoCollection, i[0], i[1]));
            } catch (Exception e) {
                // TODOed Auto-generated catch block
                e.printStackTrace();
            }
        });
    
        mongoClient.close();
    
        result.stream().forEach(r -> System.out.println(r));
    
        Date eDate = new Date();
        System.out.printf("==================================   End time => %s, %d in milliseconds\n\n", eDate, (eDate.getTime() - sDate.getTime()));
    
    }

    @Test
    public void testFile0() {

        MongoBean bean = new MongoBean();
        bean.setUri(JavaUtilTest.getMyUri());
        String collection = bean.getCollection();
        String database = bean.getDatabase();
        MongoClient mongoClient = bean.mongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        MongoCollection mongoCollection = mongoDatabase.getCollection(collection);

        Date sDate = new Date();
        System.out.printf("================================== Start time => %s\n\n", sDate);

        String filename = FilenameEnum.F0.getCode();
        List<String[]> inputDataList = parseFile(filename);
        // FIMXE no print
        // inputDataList.stream().forEach(r -> System.out.printf("Coulmn 0= %s, Column 1=%s\n", r[0], r[1]));

        Date fDate = new Date();
        System.out.printf("==================================   Parse File Process time => %d in milliseconds\n\n", (fDate.getTime() - sDate.getTime()));

        List<String> result = new ArrayList<String>();

        // FIXME
        // by loop...
        // for (String[] i: inputDataList) {
        // result.add(javaUtil.queryByDeviceAndId(mongoCollection, i[0], i[1]));
        // }
        inputDataList.stream().forEach(i -> {
            try {
                result.add(JavaUtil.queryByDeviceAndId(mongoCollection, i[0], i[1]));
            } catch (Exception e) {
                // TODOed Auto-generated catch block
                e.printStackTrace();
            }
        });

        mongoClient.close();

        result.stream().forEach(r -> System.out.println(r));

        Date eDate = new Date();
        System.out.printf("==================================   End time => %s, %d in milliseconds\n\n", eDate, (eDate.getTime() - sDate.getTime()));

    }

    /**
     * Parse test data file.
     * 
     * @param filename String
     * @return List of result array List<String[]>
     */
    private List<String[]> parseFile(String filename) {

        // use comma as separator
        String splitRegex = ",";
        int limit = 3;

        List<String[]> resultList = new ArrayList<String[]>();

        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(filename));
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(splitRegex, limit);
                String[] ary = { cols[0], cols[1] };
                resultList.add(ary);
            }
        } catch (FileNotFoundException e1) {
            // TODOed Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {
            // TODOed Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODOed Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return resultList;
    }

    /**
     * <ol>
     * <li>D:/myProject/MICB/POCII/TIDFE0019_1.D.csv
     * <li>D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_1.D.csv
     * <li>D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_2.D.csv
     * <li>D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_3.D.csv
     * <li>D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_4.D.csv
     * <li>D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_5.D.csv
     * <li>D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_6.D.csv
     * <li>D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_7.D.csv
     * <li>D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_8.D.csv
     * <li>D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_9.D.csv
     * <li>D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_10.D.csv
     * </ol>
     */
    enum FilenameEnum {

        F1m("D:/myProject/MICB/POCII/TIDFE0019_1.1M.csv"),
        F0("D:/myProject/MICB/POCII/TIDFE0019_1.D.csv"),
        F1("D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_1.D.csv"),
        F2("D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_2.D.csv"),
        F3("D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_3.D.csv"),
        F4("D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_4.D.csv"),
        F5("D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_5.D.csv"),
        F6("D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_6.D.csv"),
        F7("D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_7.D.csv"),
        F8("D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_8.D.csv"),
        F9("D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_9.D.csv"),
        F10("D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_10.D.csv");

        private String code;

        FilenameEnum(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    @Test
    public void testFile1() {

        MongoBean bean = new MongoBean();
        bean.setUri(JavaUtilTest.getMyUri());
        String collection = bean.getCollection();
        String database = bean.getDatabase();
        MongoClient mongoClient = bean.mongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        MongoCollection mongoCollection = mongoDatabase.getCollection(collection);

        Date sDate = new Date();
        System.out.printf("================================== Start time => %s\n\n", sDate);

        String filename = FilenameEnum.F1.getCode();
        List<String[]> inputDataList = parseFile(filename);
        inputDataList.stream().forEach(r -> System.out.printf("Coulmn 0= %s, Column 1=%s\n", r[0], r[1]));

        Date fDate = new Date();
        System.out.printf("==================================   Parse File Process time => %d in milliseconds\n\n", (fDate.getTime() - sDate.getTime()));

        List<String> result = new ArrayList<String>();

        // by loop...
        // for (String[] i: inputDataList) {
        // result.add(javaUtil.queryByDeviceAndId(mongoCollection, i[0], i[1]));
        // }
        inputDataList.stream().forEach(i -> {
            try {
                result.add(JavaUtil.queryByDeviceAndId(mongoCollection, i[0], i[1]));
            } catch (Exception e) {
                // TODOed Auto-generated catch block
                e.printStackTrace();
            }
        });

        mongoClient.close();

        result.stream().forEach(r -> System.out.println(r));

        Date eDate = new Date();
        System.out.printf("==================================   End time => %s, %d in milliseconds\n\n", eDate, (eDate.getTime() - sDate.getTime()));

    }

}
