/**
 * 
 */
package com.iisigroup.cap.app.config;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

/**
 * @author 1104300
 *
 */
@Configuration
@ConfigurationProperties(prefix = "mongodb.doc")
public class MongodbConfig {

    private String uri = "mongodb://sk:sk@localhost:27017";

    private String collection = "array_test";

    private String database = "stage";

    public enum ParamEnum {

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

    public enum DocEnum {

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

	@Bean
	public MongoClient mongoClient() {
		MongoClientURI mongoClientURI = new MongoClientURI(uri);
		MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
		builder.connectionsPerHost(3000);

		MongoClientOptions mongoClientOptions = builder.build();

		MongoClient mongoClient = new MongoClient(mongoClientURI);
		return mongoClient;
	}

	@Bean
	public MongoDatabase mongoDatabase() {
		MongoDatabase mongoDatabase = mongoClient().getDatabase(database);
		return mongoDatabase;
	}
	
	@Bean
	public AtomicLong atomicLong(){
		return new AtomicLong();
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

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

}
