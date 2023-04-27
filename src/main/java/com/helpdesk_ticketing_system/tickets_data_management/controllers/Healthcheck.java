package com.helpdesk_ticketing_system.tickets_data_management.controllers;

import com.helpdesk_ticketing_system.tickets_data_management.entities.TicketDocument;
import com.helpdesk_ticketing_system.tickets_data_management.persistence.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class Healthcheck {
    private final Database<TicketDocument> ticketDocumentDatabase;

    @Autowired
    public Healthcheck(Database<TicketDocument> ticketDocumentDatabase) {
        this.ticketDocumentDatabase = ticketDocumentDatabase;
    }

    @GetMapping
    public ResponseEntity<Object> sendHealthStat(){
        ticketDocumentDatabase.getById("health_check", TicketDocument.class);
        return ResponseEntity.ok(null);
    }
}
