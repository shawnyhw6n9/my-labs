package com.iisigroup.cap.app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import tool.JavaUtil;

/**
 * <pre>
 * Spring Boot Application.
 * </pre>
 * 
 * @since May 2, 2019
 * @author Sunkist Wang
 * @version
 *          <ul>
 *          <li>May 2, 2019,Sunkist Wang,new
 *          </ul>
 */
@SpringBootApplication(scanBasePackages = { "com.iisigroup.cap.app", "com.iisigroup.cap.app.config", "tool" })
public class CapApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CapApplication.class, args);
    }

    @Value("${mongodb.doc.collection:}")
    String collection;

    @Value("${mongodb.doc.inputFilename:./test.csv}")
    String inputFilename;

    @Autowired(required = false)
    private MongoDatabase mongoDatabase;

    @Autowired(required = false)
    private MongoClient mongoClient;

    @Autowired(required = false)
    private JavaUtil javaUtil;

    @Override
    public void run(String... args) throws Exception {

        System.out.printf("Test Type => %s\n", args[1]);

        if ("1".equals(args[1])) {
            delAll();
        } else if ("2".equals(args[1])) {
            test();
        } else if ("3".equals(args[1])) {
            testFile0();
        } else if ("4".equals(args[1])) {
            testFile1m();
        } else if ("5".equals(args[1])) {
            testFile1();
        }

    }

    public void test() {

        MongoCollection mongoCollection = mongoDatabase.getCollection(collection);

        Date sDate = new Date();
        System.out.printf("================================== Start time => %s\n\n", sDate);

        try {

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

    public void delAll() {
        MongoCollection mongoCollection = mongoDatabase.getCollection(collection);

        mongoCollection.deleteMany(Filters.or(Filters.in("DeviceId", "D1"), Filters.in("DeviceId", "D2"), Filters.in("DeviceId", "D3"), Filters.in("DeviceId", "D4"), Filters.in("DeviceId", "D5"),
                Filters.in("Id", "A456")));

        // FIXME
//        mongoCollection.drop();

        mongoClient.close();
    }

    public void testFile0() {

        MongoCollection mongoCollection = mongoDatabase.getCollection(collection);

        Date sDate = new Date();
        System.out.printf("================================== Start time => %s\n\n", sDate);

        String filename = FilenameEnum.F0.getCode();
        List<String[]> inputDataList = parseFile(filename);
        // FIMXE no print
        // inputDataList.stream().forEach(r -> System.out.printf("Coulmn 0= %s, Column
        // 1=%s\n", r[0], r[1]));

        Date fDate = new Date();
        System.out.printf("==================================   Parse File Process time => %d in milliseconds\n\n", (fDate.getTime() - sDate.getTime()));

        List<String> result = new ArrayList<String>();

        inputDataList.stream().forEach(i -> {
            try {
                result.add(javaUtil.queryByDeviceAndId(mongoCollection, i[0], i[1]));
            } catch (Exception e) {
                // TODOed Auto-generated catch block
                e.printStackTrace();
            }
        });

        mongoClient.close();

        // result.stream().forEach(r -> System.out.println(r));

        Date eDate = new Date();
        System.out.printf("==================================   End time => %s, %d in milliseconds\n\n", eDate, (eDate.getTime() - sDate.getTime()));

    }

    public void testFile1m() {

        MongoCollection mongoCollection = mongoDatabase.getCollection(collection);

        Date sDate = new Date();
        System.out.printf("================================== Start time => %s\n\n", sDate);

        String filename = !javaUtil.isEmpty(inputFilename) ? inputFilename : FilenameEnum.F1m.getCode();
        List<String[]> inputDataList = parseFile(filename);
        // FIMXE no print
        // inputDataList.stream().forEach(r -> System.out.printf("Coulmn 0= %s, Column
        // 1=%s\n", r[0], r[1]));

        Date fDate = new Date();
        System.out.printf("==================================   Parse File Process time => %d in milliseconds\n\n", (fDate.getTime() - sDate.getTime()));

        List<String> result = new ArrayList<String>();

        inputDataList.stream().forEach(i -> {
            try {
                result.add(javaUtil.queryByDeviceAndId(mongoCollection, i[0], i[1]));
            } catch (Exception e) {
                // TODOed Auto-generated catch block
                e.printStackTrace();
            }
        });

        mongoClient.close();

        // FIXME
        // result.stream().forEach(r -> System.out.println(r));

        Date eDate = new Date();
        System.out.printf("==================================   End time => %s, %d in milliseconds\n\n", eDate, (eDate.getTime() - sDate.getTime()));

    }

    public void testFile1() {

        MongoCollection mongoCollection = mongoDatabase.getCollection(collection);

        Date sDate = new Date();
        System.out.printf("================================== Start time => %s\n\n", sDate);

        String filename = FilenameEnum.F1.getCode();
        List<String[]> inputDataList = parseFile(filename);
        inputDataList.stream().forEach(r -> System.out.printf("Coulmn 0= %s, Column 1=%s\n", r[0], r[1]));

        Date fDate = new Date();
        System.out.printf("==================================   Parse File Process time => %d in milliseconds\n\n", (fDate.getTime() - sDate.getTime()));

        List<String> result = new ArrayList<String>();

        inputDataList.stream().forEach(i -> {
            try {
                result.add(javaUtil.queryByDeviceAndId(mongoCollection, i[0], i[1]));
            } catch (Exception e) {
                // TODOed Auto-generated catch block
                e.printStackTrace();
            }
        });

        mongoClient.close();

        // result.stream().forEach(r -> System.out.println(r));

        Date eDate = new Date();
        System.out.printf("==================================   End time => %s, %d in milliseconds\n\n", eDate, (eDate.getTime() - sDate.getTime()));

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

}