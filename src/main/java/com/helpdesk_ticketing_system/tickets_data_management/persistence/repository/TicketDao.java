package com.helpdesk_ticketing_system.tickets_data_management.persistence.repository;

import com.helpdesk_ticketing_system.tickets_data_management.entities.TicketDocument;

import java.util.List;

public interface TicketDao {
    String saveTicket(TicketDocument ticketDocument) throws Exception;
    List<TicketDocument> getTickets(String deptId, String status, Integer limit, Long startRange, Long endRange);
    TicketDocument getTicketById(Object ticketId);
}
