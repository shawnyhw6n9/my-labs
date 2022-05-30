/**
 * 
 */
package com.iisigroup.cap.app.config;

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

	private String uri;

	private String database;

	private String collection;

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
