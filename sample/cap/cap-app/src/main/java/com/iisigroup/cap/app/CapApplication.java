package com.iisigroup.cap.app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Value("${mongodb.doc.inputFilename:}")
    String inputFilename;

    @Autowired(required = false)
    private MongoDatabase mongoDatabase;

    @Autowired(required = false)
    private MongoClient mongoClient;

    @Autowired(required = false)
    private JavaUtil javaUtil;

    @Override
    public void run(String... args) throws Exception {

        List<String> collect = IntStream.range(0, args.length).mapToObj(i -> args[i]).collect(Collectors.toList());
        collect.stream().forEach(System.out::println);

        if (args.length > 1) {
            
            System.out.printf("Test Type => %s\n", args[1]);

            if ("1".equals(args[1])) {
                delAll();
            } else if ("2".equals(args[1])) {
                test();
            } else if ("3".equals(args[1])) {
                testFileOneLine();
            } else if ("4".equals(args[1])) {
                testFile();
            }
        }else {
            test();
        }

    }

    public void test() {

        MongoCollection mongoCollection = mongoDatabase.getCollection(collection);

        Long sDate = System.currentTimeMillis();
        
        System.out.printf("================================== Start time => %s\n\n{$or : [\n\n", sDate);

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

            List<String> collect = IntStream.range(0, result.size()).mapToObj(index -> String.format("count= %d%s {UID: \"%s\"}", index, (index == 0 ? " " : ", "), result.get(index)))
                    .collect(Collectors.toList());

            collect.stream().forEach(System.out::println);

        } catch (Exception e) {
            // TODOed Auto-generated catch block
            e.printStackTrace();
        }

        Long eDate = System.currentTimeMillis();

        System.out.printf("\n]}\n\n==================================   Start time => %s, End time => %s, %d in milliseconds\n\n", sDate, eDate, (eDate - sDate));
    }

    public void delAll() {
        MongoCollection mongoCollection = mongoDatabase.getCollection(collection);

        mongoCollection.deleteMany(Filters.or(Filters.in("DeviceId", "D1"), Filters.in("DeviceId", "D2"), Filters.in("DeviceId", "D3"), Filters.in("DeviceId", "D4"), Filters.in("DeviceId", "D5"),
                Filters.in("Id", "A456")));

        mongoClient.close();
    }

    public void testFileOneLine() {

        MongoCollection mongoCollection = mongoDatabase.getCollection(collection);

        Long sDate = System.currentTimeMillis();

        System.out.printf("================================== Start time => %s\n\n", sDate);

        String filename = !javaUtil.isEmpty(inputFilename) ? inputFilename : FilenameEnum.F1201.getCode();

        // FIMXE no print
        // inputDataList.stream().forEach(r -> System.out.printf("Coulmn 0= %s, Column
        // 1=%s\n", r[0], r[1]));

        Long fDate = System.currentTimeMillis();

        System.out.printf("==================================   Parse File Process time => %d in milliseconds\n\n", (fDate - sDate));

        try {
            parseFileAndQuery(filename, mongoCollection, javaUtil);
        } catch (Exception e) {
            // TODOed Auto-generated catch block
            e.printStackTrace();
        } finally {
            mongoClient.close();
        }

        Long eDate = System.currentTimeMillis();
        
        System.out.printf("==================================   Start time => %s, End time => %s, %d in milliseconds\n\n", sDate, eDate, (eDate - sDate));

    }

    public void testFile() {

        MongoCollection mongoCollection = mongoDatabase.getCollection(collection);

        Long sDate = System.currentTimeMillis();
        System.out.printf("================================== Start time => %s\n\n", sDate);

        String filename = !javaUtil.isEmpty(inputFilename) ? inputFilename : FilenameEnum.F3010.getCode();
        List<String[]> inputDataList = parseFile(filename);

        // FIMXE no print
        // inputDataList.stream().forEach(r -> System.out.printf("Coulmn 0= %s, Column
        // 1=%s\n", r[0], r[1]));

        Long fDate = System.currentTimeMillis();
        System.out.printf("==================================   Parse File Process time => %d in milliseconds\n\n", (fDate - sDate));

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

        mongoClient.close();
        List<String> collect = IntStream.range(0, result.size()).mapToObj(index -> "count= " + index + " { UID: \"" + result.get(index) + "\"}").collect(Collectors.toList());

        // result.stream().forEach(r -> System.out.println(r));
        collect.stream().forEach(System.out::println);

        Long eDate = System.currentTimeMillis();
        
        System.out.printf("==================================   Start time => %s, End time => %s, %d in milliseconds\n\n", sDate, eDate, (eDate - sDate));

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
        int i = 0;
        int j = 1;

        if (filename.endsWith("Norman.csv")) {
            i = 3;
            j = 4;
        }

        List<String[]> resultList = new ArrayList<String[]>();

        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(filename));
            int c = 0;
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(splitRegex);
                String r = javaUtil.queryByDeviceAndId(mongoCollection, cols[i], cols[j]);
                System.out.printf("count= %d, %s\n", ++c, r);
            }
        } catch (FileNotFoundException e1) {
            // TODOed Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {
            // TODOed Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
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
        int i = 0;
        int j = 1;

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
        F10("D:/myProject/MICB/POCII/IDFE0019_10W/TIDFE0019_10.D.csv"),
        F301("/Users/shawnyhw6n9/Downloads/full-channel/IDFE0019_30W/TIDFE0019_1.D.csv"),
        F302("/Users/shawnyhw6n9/Downloads/full-channel/IDFE0019_30W/TIDFE0019_2.D.csv"),
        F303("/Users/shawnyhw6n9/Downloads/full-channel/IDFE0019_30W/TIDFE0019_3.D.csv"),
        F304("/Users/shawnyhw6n9/Downloads/full-channel/IDFE0019_30W/TIDFE0019_4.D.csv"),
        F305("/Users/shawnyhw6n9/Downloads/full-channel/IDFE0019_30W/TIDFE0019_5.D.csv"),
        F306("/Users/shawnyhw6n9/Downloads/full-channel/IDFE0019_30W/TIDFE0019_6.D.csv"),
        F307("/Users/shawnyhw6n9/Downloads/full-channel/IDFE0019_30W/TIDFE0019_7.D.csv"),
        F308("/Users/shawnyhw6n9/Downloads/full-channel/IDFE0019_30W/TIDFE0019_8.D.csv"),
        F309("/Users/shawnyhw6n9/Downloads/full-channel/IDFE0019_30W/TIDFE0019_9.D.csv"),
        F3010("/Users/shawnyhw6n9/Downloads/full-channel/IDFE0019_30W/TIDFE0019_10.D.csv"),
        F1201("/Users/shawnyhw6n9/Downloads/full-channel/TIDFE0019_120w.D.CSV");

        private String code;

        FilenameEnum(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

}