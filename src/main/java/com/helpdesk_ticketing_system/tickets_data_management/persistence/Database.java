package com.helpdesk_ticketing_system.tickets_data_management.persistence;

import java.util.List;

public interface Database<T> {
    T saveToDb(T newData) throws Exception;
    List<T> getFromDb(String deptId, String status, Integer limit, Long startRange, Long endRange, Class<T> targetType);
    T getById(Object ticketId, Class<T> targetType);
}
