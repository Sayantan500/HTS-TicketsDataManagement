package com.helpdesk_ticketing_system.tickets_data_management.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetTicketResponse
{
    @JsonProperty("count") private Integer count;
    @JsonProperty("records") private List<TicketDocument> records;
    @JsonProperty("posted_on_for_first_record") private Long postedOnValOfFirstDataItem;
    @JsonProperty("posted_on_for_end_record") private Long postedOnValOfEndDataItem;

    public GetTicketResponse() {
    }

    public GetTicketResponse(Integer count, List<TicketDocument> records, Long postedOnValOfFirstDataItem, Long postedOnValOfEndDataItem) {
        this.count = count;
        this.records = records;
        this.postedOnValOfFirstDataItem = postedOnValOfFirstDataItem;
        this.postedOnValOfEndDataItem = postedOnValOfEndDataItem;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<TicketDocument> getRecords() {
        return records;
    }

    public void setRecords(List<TicketDocument> records) {
        this.records = records;
    }

    public Long getPostedOnValOfFirstDataItem() {
        return postedOnValOfFirstDataItem;
    }

    public void setPostedOnValOfFirstDataItem(Long postedOnValOfFirstDataItem) {
        this.postedOnValOfFirstDataItem = postedOnValOfFirstDataItem;
    }

    public Long getPostedOnValOfEndDataItem() {
        return postedOnValOfEndDataItem;
    }

    public void setPostedOnValOfEndDataItem(Long postedOnValOfEndDataItem) {
        this.postedOnValOfEndDataItem = postedOnValOfEndDataItem;
    }
}
