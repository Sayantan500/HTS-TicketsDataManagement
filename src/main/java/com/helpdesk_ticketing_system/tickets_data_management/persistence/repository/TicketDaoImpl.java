package com.helpdesk_ticketing_system.tickets_data_management.persistence.repository;

import com.helpdesk_ticketing_system.tickets_data_management.entities.TicketDocument;
import com.helpdesk_ticketing_system.tickets_data_management.persistence.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class TicketDaoImpl implements TicketDao{

    private final Database<TicketDocument> ticketDocumentDatabase;

    @Autowired
    public TicketDaoImpl(@Qualifier("mongodb") Database<TicketDocument> ticketDocumentDatabase) {
        this.ticketDocumentDatabase = ticketDocumentDatabase;
    }

    @Override
    public String saveTicket(TicketDocument ticketDocument) throws Exception {
        return ticketDocumentDatabase.saveToDb(ticketDocument).get_id();
    }
}
