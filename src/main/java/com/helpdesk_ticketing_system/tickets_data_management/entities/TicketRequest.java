package com.helpdesk_ticketing_system.tickets_data_management.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketRequest {

    @JsonProperty("message")
    private String message;
    @JsonProperty("assigned_to")
    private String departmentId;

    public TicketRequest() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "TicketRequest{" +
                "message='" + message + '\'' +
                ", departmentId='" + departmentId + '\'' +
                '}';
    }
}
