package com.helpdesk_ticketing_system.tickets_data_management.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ticket {
    @JsonProperty("message")
    private String message;

    public Ticket() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "message='" + message + '\'' +
                '}';
    }
}
