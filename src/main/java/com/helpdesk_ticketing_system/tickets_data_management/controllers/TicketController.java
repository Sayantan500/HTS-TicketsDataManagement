package com.helpdesk_ticketing_system.tickets_data_management.controllers;

import com.helpdesk_ticketing_system.tickets_data_management.entities.Status;
import com.helpdesk_ticketing_system.tickets_data_management.entities.Ticket;
import com.helpdesk_ticketing_system.tickets_data_management.entities.TicketDocument;
import com.helpdesk_ticketing_system.tickets_data_management.entities.TicketRequest;
import com.helpdesk_ticketing_system.tickets_data_management.persistence.repository.TicketDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Stack;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketDao ticketDao;

    @Autowired
    public TicketController(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    @PostMapping
    public ResponseEntity<Object> saveTicket(@RequestBody TicketRequest ticketRequest) throws Exception {
        System.out.println("ticketRequest = " + ticketRequest);
        TicketDocument ticketDocument=new TicketDocument();
        Ticket ticket=new Ticket();
        ticket.setMessage(ticketRequest.getMessage());
        ticketDocument.setDepartmentId(ticketRequest.getDepartmentId());
        ticketDocument.setTicket(ticket);
        //TODO:ticketDocument.set_id();
        ticketDocument.setStatus(Status.TICKET_RAISED.name());
        ticketDocument.setPostedOn(System.currentTimeMillis());

        String save = ticketDao.saveTicket(ticketDocument);
        return new ResponseEntity<>(save,HttpStatus.OK);
    }
}
