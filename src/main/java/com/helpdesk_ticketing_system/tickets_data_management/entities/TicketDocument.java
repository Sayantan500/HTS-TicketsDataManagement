package com.helpdesk_ticketing_system.tickets_data_management.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
public class TicketDocument {
    @MongoId(value = FieldType.STRING)
    private String _id;
    @Field("issue_id")
    private String issueId;
    @JsonProperty("status") @Field("status") private String status;
    @Field("department_id") private String departmentId;
    @JsonProperty("posted_on") @Field("posted_on") private Long postedOn;
    @JsonProperty("ticket") @Field("ticket") private Ticket ticket;

    public TicketDocument() {
    }

    @JsonGetter("ticket_id")
    public String get_id() {
        return _id;
    }

    @JsonSetter("_id")
    public void set_id(String _id) {
        this._id = _id;
    }

    @JsonGetter("issue_id")
    public String getIssueId() {
        return issueId;
    }

    @JsonSetter("issue_id")
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonGetter("assigned_to")
    public String getDepartmentId() {
        return departmentId;
    }

    @JsonSetter("department_id")
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
