package com.helpdesk_ticketing_system.tickets_data_management.controllers;

import com.helpdesk_ticketing_system.tickets_data_management.entities.TicketRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @PostMapping
    public ResponseEntity<Object> saveTicket(@RequestBody TicketRequest ticketRequest)
    {
        System.out.println("ticketRequest = " + ticketRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
