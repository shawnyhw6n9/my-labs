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

import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

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

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseEntity<?> queryByDevice(@RequestBody Map<String, Object> datas) {

        log.debug("MBID is : {}", datas.get("MBID"));

        MongoClientURI mongoClientURI = new MongoClientURI(uri);

        try (MongoClient mongoClient = new MongoClient(mongoClientURI)) {

            MongoDatabase mongoDatabase = mongoClient.getDatabase(database);

            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);

            FindIterable<Document> findIterable = mongoCollection.find();

            MongoCursor<Document> mongoCursor = findIterable.iterator();

            StringBuffer buff = new StringBuffer();

            while (mongoCursor.hasNext()) {

                log.debug("{}", buff.append(mongoCursor.next().toJson()));

            }

            mongoClient.close();

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(buff.toString());
        }

    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<?> findAll() {

        // try ( MongoClient mongoClient = new MongoClient("localhost", 27017)) {

        // MongoCredential credential = MongoCredential.createCredential("poc_mega", "stage", "hk4g4rufu4".toCharArray());

        // MongoCredential credential = MongoCredential.createCredential("sk", "admin", "sk".toCharArray());
        // try (MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017), Arrays.asList(credential))) {

        MongoClientURI mongoClientURI = new MongoClientURI(uri);

        try (MongoClient mongoClient = new MongoClient(mongoClientURI)) {

            MongoDatabase mongoDatabase = mongoClient.getDatabase(database);

            log.debug("Connect to database successfully");

            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);

            FindIterable<Document> findIterable = mongoCollection.find();

            MongoCursor<Document> mongoCursor = findIterable.iterator();

            StringBuffer buff = new StringBuffer();

            while (mongoCursor.hasNext()) {

                log.debug("{}", buff.append(mongoCursor.next().toJson()));

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
