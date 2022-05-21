/* 
 * DocController.java
 * 
 * Copyright (c) 2019 International Integrated System, Inc. 
 * All Rights Reserved.
 * 
 * Licensed Materials - Property of International Integrated System, Inc.
 * 
 * This software is confidential and proprietary information of 
 * International Integrated System, Inc. (&quot;Confidential Information&quot;).
 */
package com.iisigroup.cap.app.controller;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/**
 * <pre>
 * Controller for document that in Mongodb
 * </pre>
 * 
 * @since 2022年5月20日
 * @author 1104300
 * @version
 *          <ul>
 *          <li>2022年5月20日,1104300,new
 *          </ul>
 */
@RestController
@RequestMapping("/doc")
@ConfigurationProperties(prefix = "mogodb.doc")
public class DocController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    // @Value("${mogodb.doc.uri:}")
    private String uri;

    // @Value("${mogodb.doc.database:}")
    private String database;

    // @Value("${mogodb.doc.collection:}")
    private String collection;

    public static final String REQUEST_DEVICE_ID = "MBID";
    public static final String REQUEST_ID = "ID";

    public static final String OBJECT_ID = "_id";
    public static final String DEVICE_ID = "MB";
    public static final String GOLOBAL_ID = "GID";
    public static final String UNIVERSAL_ID = "UID";

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseEntity<?> queryByDevice(@RequestBody Map<String, Object> datas) {

        String requestMbId = datas.getOrDefault(REQUEST_DEVICE_ID, "").toString();
        String requestId = datas.getOrDefault(REQUEST_ID, "").toString();

        log.debug("MBID is : {}, ID is : {}", requestMbId, requestId);

        MongoClientURI mongoClientURI = new MongoClientURI(uri);

        try (MongoClient mongoClient = new MongoClient(mongoClientURI)) {

            MongoDatabase mongoDatabase = mongoClient.getDatabase(database);

            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);

            // CASE A : 依 Device Id 篩選查找
            FindIterable<Document> findIterable = mongoCollection.find(Filters.or(Filters.in(DEVICE_ID, requestMbId), Filters.eq(DEVICE_ID, requestMbId))).limit(1);

            MongoCursor<Document> mongoCursor = findIterable.iterator();

            StringBuffer buff = new StringBuffer();

            // 篩選條件下的筆數 (須參考 find().limit(1) )
            int size = 0;

            // 需上傳更新回 Mongodb 的
            List<String> mbList = new LinkedList<String>();

            Document findOutDoc = customizeDoc(mongoCursor, size, mbList);

            if (findOutDoc == null) {

                // CASE C : MBID 查不到 改以 UID 查詢
                findIterable = mongoCollection.find(Filters.eq(UNIVERSAL_ID, requestId)).limit(1);

                mongoCursor = findIterable.iterator();

                findOutDoc = customizeDoc(mongoCursor, size, mbList);

                if (findOutDoc == null) {

                    // 需要新增 Doc

                    Document document = new Document(GOLOBAL_ID, getUUID()).append(REQUEST_DEVICE_ID, requestMbId);

                    document.remove(OBJECT_ID);
                    buff.append(document.toJson());

                    mbList.add(requestMbId);
                    document.remove(REQUEST_DEVICE_ID);
                    document.append(DEVICE_ID, mbList);

                    mongoCollection.insertOne(document);

                } else {

                    // CASE B
                    // 依篩選條件更新 Doc

                    mbList.add(requestMbId);
                    Document document = new Document(DEVICE_ID, mbList);
                    mongoCollection.updateMany(Filters.eq(UNIVERSAL_ID, requestId), new Document("$set", document));

                }

            } else {

                // 依篩選條件更新 Doc

                Document document = new Document(DEVICE_ID, mbList);
                if (!requestId.isEmpty()) {
                    document.append(UNIVERSAL_ID, requestId);
                }
                mongoCollection.updateMany(Filters.or(Filters.in(DEVICE_ID, requestMbId), Filters.eq(DEVICE_ID, requestMbId)), new Document("$set", document));

            }

            if (findOutDoc != null) {
                
                findOutDoc.put(REQUEST_DEVICE_ID, requestMbId);

                if (buff.length() > 0) {
                    buff.append(",");
                }

                // 用他自身的功能轉換成 JSON 格式
                buff.append(findOutDoc.toJson());
                
            }

            mongoClient.close();

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(buff.toString());
        }

    }

    private Document customizeDoc(MongoCursor<Document> mongoCursor, int size, List<String> mbList) {

        Document doc = null;

        while (mongoCursor.hasNext()) {

            // 查出來的 Doc
            doc = mongoCursor.next();

            size++;

            doc.put(GOLOBAL_ID, doc.getOrDefault(GOLOBAL_ID, ""));

            String mb = "";
            if (doc.containsKey(DEVICE_ID)) {
                try {
                    mb = doc.getString(DEVICE_ID);
                } catch (Exception e) {
                    mbList.addAll(doc.getList(DEVICE_ID, String.class));
                    mb = mbList.isEmpty() ? "" : mbList.get(0);
                }
            }

            doc.put(REQUEST_DEVICE_ID, mb);

            // 這邊整理一下要回應的格式，不需要的就移除
            doc.remove(OBJECT_ID);
            doc.remove(DEVICE_ID);
            doc.remove(UNIVERSAL_ID);

            log.debug("{}", doc);

            // 僅取一筆
            break;

        }
        return doc;
    }

    /**
     * Get the UUID
     * 
     * @return
     */
    private Object getUUID() {
        return "A" + genRandInt(100000000, 999999999)+ "B"+ genRandInt(100000000, 999999999);
    }

    /**
     * Random the min to max number
     * 
     * @param min
     * @param max
     * @return
     */
    public int genRandInt(int min, int max) {
        SecureRandom rand = getRandom();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Gen Securn random
     * 
     * @return
     */
    private SecureRandom getRandom() {
        SecureRandom rand = null;
        try {
            rand = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
        }
        return rand;
    }

    @RequestMapping(value = "/delAll", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAll(@RequestBody Map<String, Object> datas) {

        String requstMbId = datas.getOrDefault(REQUEST_DEVICE_ID, "").toString();

        log.debug("MBID is : {}", requstMbId);

        MongoClientURI mongoClientURI = new MongoClientURI(uri);
        try (MongoClient mongoClient = new MongoClient(mongoClientURI)) {

            MongoDatabase mongoDatabase = mongoClient.getDatabase(database);

            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);

            // CASE A : 依 Device Id 篩選查找
            FindIterable<Document> findIterable = mongoCollection.find(Filters.or(Filters.in(DEVICE_ID, requstMbId), Filters.eq(DEVICE_ID, requstMbId))).limit(1);

            mongoCollection.deleteMany(Filters.or(Filters.in(DEVICE_ID, requstMbId), Filters.eq(DEVICE_ID, requstMbId)));

            mongoClient.close();

            return ResponseEntity.ok().build();
        }
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<?> findAll() {

        // try ( MongoClient mongoClient = new MongoClient("localhost", 27017)) {

        // MongoCredential credential = MongoCredential.createCredential("poc_mega", "stage", "hk4g4rufu4".toCharArray());

        // MongoCredential credential = MongoCredential.createCredential("sk", "admin", "sk".toCharArray());
        // try (MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017), Arrays.asList(credential))) {

        // 準備好 mongodb 連線資訊 uri
        MongoClientURI mongoClientURI = new MongoClientURI(uri);

        try (MongoClient mongoClient = new MongoClient(mongoClientURI)) {

            MongoDatabase mongoDatabase = mongoClient.getDatabase(database);

            log.debug("Connect to database successfully");

            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);

            // 這邊先查詢出全部
            FindIterable<Document> findIterable = mongoCollection.find();

            MongoCursor<Document> mongoCursor = findIterable.iterator();

            StringBuffer buff = new StringBuffer();

            // 全部輪詢記錄下來
            while (mongoCursor.hasNext()) {

                Document doc = mongoCursor.next();
                doc.remove(OBJECT_ID);
                if (buff.length() > 0) {
                    buff.append(",");
                }
                // 用他自身的功能轉換成 JSON 格式
                buff.append(doc.toJson());
                log.debug("{}", doc);

            }

            mongoClient.close();

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(buff.toString());

        } finally {

        }

    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri
     *            the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return the database
     */
    public String getDatabase() {
        return database;
    }

    /**
     * @param database
     *            the database to set
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * @return the collection
     */
    public String getCollection() {
        return collection;
    }

    /**
     * @param collection
     *            the collection to set
     */
    public void setCollection(String collection) {
        this.collection = collection;
    }

}
