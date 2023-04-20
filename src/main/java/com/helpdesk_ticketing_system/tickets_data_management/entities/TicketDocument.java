package com.helpdesk_ticketing_system.tickets_data_management.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketDocument {
    @JsonProperty("ticket_id") private String _id;
    @JsonProperty("issue_id") private String issueId;
    @JsonProperty("status") private String status;
    @JsonProperty("assigned_to") private String departmentId;
    @JsonProperty("posted_on") private Long postedOn;
    @JsonProperty("ticket") private Ticket ticket;

    public TicketDocument() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public Long getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(Long postedOn) {
        this.postedOn = postedOn;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "TicketDocument{" +
                "_id='" + _id + '\'' +
                ", issueId='" + issueId + '\'' +
                ", status='" + status + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", postedOn=" + postedOn +
                ", ticket=" + ticket +
                '}';
    }
}
