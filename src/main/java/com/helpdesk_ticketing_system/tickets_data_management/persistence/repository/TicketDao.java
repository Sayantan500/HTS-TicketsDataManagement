package com.helpdesk_ticketing_system.tickets_data_management.persistence.repository;

import com.helpdesk_ticketing_system.tickets_data_management.entities.TicketDocument;

public interface TicketDao {
    String saveTicket(TicketDocument ticketDocument) throws Exception;
}
