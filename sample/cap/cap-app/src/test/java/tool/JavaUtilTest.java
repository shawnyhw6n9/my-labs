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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

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

    public static String MY_URI = "mongodb://sk:sk@localhost:27017";
    public static String MY_DB_NAME = "stage";
    public static String MY_COLLECTION = "array_test";
    public static String FILENAME = "./test.csv";

    private MongoClientURI mongoClientURI = new MongoClientURI(MY_URI);

    private MongoClient mongoClient = new MongoClient(mongoClientURI);

    private MongoDatabase mongoDatabase = mongoClient.getDatabase(MY_DB_NAME);

    private JavaUtil javaUtil = new JavaUtil();

    @Test
    public void test() {

        MongoCollection mongoCollection = mongoDatabase.getCollection(MY_COLLECTION);

        javaUtil.setCollection(MY_COLLECTION);

        Date sDate = new Date();
        System.out.printf("================================== Start time => %s\n\n{$or : [\n\n", sDate);

        try {

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

            List<String> collect = IntStream.range(0, result.size()).mapToObj(index -> String.format("count= %d%s {UID: \"%s\"}",  index , (index == 0 ? " " : ", "), result.get(index))).collect(Collectors.toList());
            collect.stream().forEach(System.out::println);
            
            mongoClient.close();

        } catch (Exception e) {
            // TODOed Auto-generated catch block
            e.printStackTrace();
        }

        Date eDate = new Date();
        System.out.printf("\n]}\n\n==================================   End time => %s, %d in milliseconds\n\n", eDate, (eDate.getTime() - sDate.getTime()));
    }

    @Test
    public void delAll() {
        MongoCollection mongoCollection = mongoDatabase.getCollection(MY_COLLECTION);

        javaUtil.setCollection(MY_COLLECTION);

        DeleteResult r = mongoCollection.deleteMany(Filters.or(Filters.in("DeviceId", "D1"), Filters.in("DeviceId", "D2"), Filters.in("DeviceId", "D3"), Filters.in("DeviceId", "D4"),
                Filters.in("DeviceId", "D5"), Filters.in("Id", "A456")));
        System.out.printf("%s\n", r);

        mongoClient.close();
    }

    @Test
    public void testFileOneLine() {

        MongoCollection mongoCollection = mongoDatabase.getCollection(MY_COLLECTION);

        javaUtil.setCollection(MY_COLLECTION);

        Date sDate = new Date();
        System.out.printf("================================== Start time => %s\n\n", sDate);

        String filename = FILENAME;

        try {
            parseFileAndQuery(filename, mongoCollection, javaUtil);
        } catch (Exception e) {
            // TODOed Auto-generated catch block
            e.printStackTrace();
        } finally {
            mongoClient.close();
        }

        Date eDate = new Date();
        System.out.printf("==================================   End time => %s, %d in milliseconds\n\n", eDate, (eDate.getTime() - sDate.getTime()));

    }

    @Test
    public void testFile() {

        MongoCollection mongoCollection = mongoDatabase.getCollection(MY_COLLECTION);

        javaUtil.setCollection(MY_COLLECTION);

        Date sDate = new Date();
        System.out.printf("================================== Start time => %s\n\n", sDate);

        String filename = FILENAME;
        List<String[]> inputDataList = parseFile(filename);

        Date fDate = new Date();
        System.out.printf("==================================   Parse File Process time => %d in milliseconds\n\n", (fDate.getTime() - sDate.getTime()));

        List<String> result = new ArrayList<String>();

        inputDataList.stream().forEach(i -> {
            try {
                String r = javaUtil.queryByDeviceAndId(mongoCollection, i[0], i[1]);
                result.add(r);
                System.out.printf("%s\n", r);
            } catch (Exception e) {
                // TODOed Auto-generated catch block
                e.printStackTrace();
            }
        });

        List<String> collect = IntStream.range(0, result.size()).mapToObj(index -> "count= "+index + ", " + result.get(index)).collect(Collectors.toList());
        // result.stream().forEach(r -> System.out.println(r));
        collect.stream().forEach(System.out::println);

        mongoClient.close();

        Date eDate = new Date();
        System.out.printf("==================================   End time => %s, %d in milliseconds\n\n", eDate, (eDate.getTime() - sDate.getTime()));

    }

    /**
     * Parse test data file with mongoCollection and javaUtil.
     * 
     * @param filename
     *            String
     * @param mongoCollection
     * @param javaUtil
     */
    private void parseFileAndQuery(String filename, MongoCollection mongoCollection, JavaUtil javaUtil) {

        // use comma as separator
        String splitRegex = ",";
        int i = 3;
        int j = 4;
        int c = 0;
        
        if (filename.endsWith("Norman.csv")) {
            i = 3;
            j = 4;
        }

        String line;
        try (FileReader fr = new FileReader(filename)){
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(splitRegex);
                String r = javaUtil.queryByDeviceAndId(mongoCollection, cols[i], cols[j]);
                c++;
                System.out.printf("count=%d, %s\n", c, r);
            }
        } catch (Exception e) {
            // TODOed Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Parse test data file.
     * 
     * @param filename
     *            String
     * @return List of result array List<String[]>
     */
    private List<String[]> parseFile(String filename) {

        // use comma as separator
        String splitRegex = ",";
        int i = 3;
        int j = 4;
        
        if (filename.endsWith("Norman.csv")) {
            i = 3;
            j = 4;
        }
        
        List<String[]> resultList = new ArrayList<String[]>();

        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(filename));
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(splitRegex);
                String[] ary = { cols[i], cols[j] };
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

}
