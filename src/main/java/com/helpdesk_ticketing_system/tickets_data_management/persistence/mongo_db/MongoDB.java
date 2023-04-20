package com.helpdesk_ticketing_system.tickets_data_management.persistence.mongo_db;

import com.helpdesk_ticketing_system.tickets_data_management.persistence.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component("mongodb")
public class MongoDB<T> implements Database<T> {
    private final MongoTemplate mongoTemplate;
    private final String collectionName;
    
    @Autowired
    public MongoDB(
            @Qualifier("mongoTemplate") MongoTemplate mongoTemplate,
            @Qualifier("mongo_collection_name") String collectionName
    ) {
        this.mongoTemplate = mongoTemplate;
        this.collectionName = collectionName;
    }

    @Override
    public T saveToDb(T newData)
    {
        return mongoTemplate.insert(newData,collectionName);
    }

    @Override
    public T getFromDbBy(String fieldName, Object fieldValue, Class<T> targetType) {
        return mongoTemplate.findOne(
                Query.query(new Criteria(fieldName).is(fieldValue)),
                targetType,
                collectionName
        );
    }

}
