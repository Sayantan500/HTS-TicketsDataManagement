package com.helpdesk_ticketing_system.tickets_data_management.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class UtilitiesImpl implements Utilities{
    private final IdGenerator idGenerator;

    @Autowired
    public UtilitiesImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public String generateId() {
        return idGenerator.generateId();
    }
}
