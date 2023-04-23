package com.helpdesk_ticketing_system.tickets_data_management.persistence.mongo_db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helpdesk_ticketing_system.tickets_data_management.persistence.Database;
import com.helpdesk_ticketing_system.tickets_data_management.utilities.LoggingUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component("mongodb")
public class MongoDB<T> implements Database<T> {
    private final MongoTemplate mongoTemplate;
    private final String collectionName;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public MongoDB(
            @Qualifier("mongoTemplate") MongoTemplate mongoTemplate,
            @Qualifier("mongo_collection_name") String collectionName,
            ObjectMapper objectMapper) {
        this.mongoTemplate = mongoTemplate;
        this.collectionName = collectionName;
        this.objectMapper = objectMapper;
    }

    @Override
    public T saveToDb(T newData)
    {
        return mongoTemplate.insert(newData,collectionName);
    }

    @Override
    public List<T> getFromDb(String deptId, String status, Integer limit, Long startRange, Long endRange, Class<T> targetType) {
        List<T> records = new LinkedList<>();
        List<Bson> queryFiltersList = new ArrayList<>();
        queryFiltersList.add(Filters.eq("department_id",deptId));
        queryFiltersList.add(Filters.eq("status",status));

        if(endRange==null || endRange.equals(startRange))
            queryFiltersList.add(Filters.gt("posted_on",startRange));
        else{
            queryFiltersList.add(Filters.gte("posted_on",startRange));
            queryFiltersList.add(Filters.lte("posted_on",endRange));
        }

        List<String> fieldsToExclude = new ArrayList<>();
        fieldsToExclude.add("ticket");

        Bson queryFilter = Filters.and(queryFiltersList);
        Bson projectionConfig = Projections.exclude(fieldsToExclude);
        FindIterable<Document> findIterable = mongoTemplate.getCollection(collectionName)
                .find(queryFilter)
                .projection(projectionConfig)
                .limit(limit);
        try (MongoCursor<Document> cursor = findIterable.cursor()) {
            cursor.forEachRemaining(document -> {
                try {
                    records.add(objectMapper.readValue(document.toJson(), targetType));
                } catch (JsonProcessingException e) {
                    LoggingUtils.logError(this.getClass(),e.getClass(),e.getMessage());
                    throw new RuntimeException(e.getMessage());
                }
            });
        } catch (Exception e){
            LoggingUtils.logError(this.getClass(),e.getClass(),e.getMessage());
            throw e;
        }
        return records;
    }

    @Override
    public T getById(Object ticketId, Class<T> targetType) {
        return mongoTemplate.findOne(
                Query.query(Criteria.where("_id").is(ticketId)),
                targetType,
                collectionName
        );
    }

    @Override
    public boolean updateStatusField(Object ticketId, Object updatedStatus) {
        UpdateResult result = mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(ticketId)),
                Update.update("status",updatedStatus),
                collectionName
        );
        return result.wasAcknowledged();
    }

}
