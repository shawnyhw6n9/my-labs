/* 
 * MongoBean.java
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

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
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
public class MongoBean {

    private String collection = "myCol";
    
    private String uri = "mongodb://sk:sk@localhost:27017";

    private String database = "test";

    enum ParamEnum {

        URI("uri"),
        DATABASE("database"),
        COLLECTION("collection");

        private String code;

        ParamEnum(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

    }
    
    enum DocEnum {

        /** DeviceId */
        REQUEST_DEVICE_ID("DeviceId"),
        /** ID */
        REQUEST_ID("ID"),
        /** _id */
        OBJECT_ID("_id"),
        /** channel id (DeviceId) */
        DEVICE_ID("DeviceId"),
        /** 全通路識別碼 (UID) */
        UID("UID"),
        /** 客戶 ID (ID) */
        ID("ID");

        private String code;

        DocEnum(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

    }


    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public MongoClient mongoClient() {
        MongoClientURI mongoClientURI = new MongoClientURI(uri);
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.connectionsPerHost(3000);

        MongoClientOptions mongoClientOptions = builder.build();

        MongoClient mongoClient = new MongoClient(mongoClientURI);
        return mongoClient;
    }

    public MongoDatabase mongoDatabase() {
        MongoDatabase mongoDatabase = mongoClient().getDatabase(database);
        return mongoDatabase;
    }
}
