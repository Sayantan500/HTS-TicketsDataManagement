package com.helpdesk_ticketing_system.tickets_data_management.persistence.mongo_db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoTypeMapper;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoDBConfig
{
    @Bean("mongo_connection_baseUri")
    String connectionBaseUri(){
        return System.getenv("mongodb_connect_uri");
    }

    @Bean("mongo_username")
    String username(){
        return System.getenv("mongodb_username");
    }

    @Bean("mongo_password")
    String password(){
        return System.getenv("mongodb_password");
    }

    @Bean("mongo_database")
    String database(){
        return System.getenv("mongo_db_name");
    }

    @Bean("mongo_collection_name")
    String mongoCollectionName(){
        return System.getenv("mongo_collection_name");
    }

    @Bean("mongoClient")
    public MongoClient mongoClient(
            @Qualifier("mongo_connection_baseUri") String baseConnectionUriString,
            @Qualifier("mongo_username") String username,
            @Qualifier("mongo_password") String password
    ){
        String connectionString = String.format(baseConnectionUriString,username,password);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(ServerApi.builder().version(ServerApiVersion.V1).build())
                .build();
        return MongoClients.create(settings);
    }
    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(
            @Qualifier("mongoClient") MongoClient mongoClient,
            @Qualifier("mongo_database") String databaseName
    ){
        return new SimpleMongoClientDatabaseFactory(mongoClient,databaseName);
    }

    @Bean("mongoTemplate")
    public MongoTemplate mongoTemplate(
            MongoDatabaseFactory mongoDatabaseFactory
    )
    {
        MongoTypeMapper mongoTypeMapper = new DefaultMongoTypeMapper(null);
        MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(
                new DefaultDbRefResolver(mongoDatabaseFactory),
                new MongoMappingContext()
        );
        mappingMongoConverter.setTypeMapper(mongoTypeMapper);
        return new MongoTemplate(mongoDatabaseFactory, mappingMongoConverter);
    }
}
