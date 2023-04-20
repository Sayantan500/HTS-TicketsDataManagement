package com.helpdesk_ticketing_system.tickets_data_management.persistence;

public interface Database<T> {
    T saveToDb(T newData) throws Exception;
    T getFromDbBy(String fieldName, Object fieldValue, Class<T> targetType);
}
