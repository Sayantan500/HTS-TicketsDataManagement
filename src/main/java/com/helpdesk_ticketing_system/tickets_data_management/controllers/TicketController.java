package com.helpdesk_ticketing_system.tickets_data_management.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helpdesk_ticketing_system.tickets_data_management.entities.*;
import com.helpdesk_ticketing_system.tickets_data_management.exceptions_handling.exceptions.InvalidParametersException;
import com.helpdesk_ticketing_system.tickets_data_management.persistence.repository.TicketDao;
import com.helpdesk_ticketing_system.tickets_data_management.utilities.LoggingUtils;
import com.helpdesk_ticketing_system.tickets_data_management.utilities.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/tickets")
public class TicketController {
    private final TicketDao ticketDao;
    private final Integer MAX_PAGINATION_LIMIT;
    private final Utilities utilities;

    @Autowired
    public TicketController(
            TicketDao ticketDao,
            @Qualifier("max_pagination_limit") Integer maxPaginationLimit,
            Utilities utilities
    ) {
        this.ticketDao = ticketDao;
        MAX_PAGINATION_LIMIT = maxPaginationLimit;
        this.utilities = utilities;
    }

    @PostMapping
    public ResponseEntity<Object> saveTicket(@RequestBody TicketRequest ticketRequest) throws Exception {
        // validating the assigned_to field,
        // must not contain any whitespace or null value
        // must have a valid department name (NOT case-sensitive)
        String assigned_to = ticketRequest.getDepartmentId();
        if(io.micrometer.common.util.StringUtils.isBlank(assigned_to)
                || io.micrometer.common.util.StringUtils.isEmpty(assigned_to)
                || StringUtils.containsWhitespace(assigned_to))
            throw new InvalidParametersException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Contains whitespace or NULL / empty value.",
                    "assigned_to",
                    HttpParameters.REQUEST_BODY
            );
        Department department ;
        try{
            department = Department.valueOf(assigned_to.toUpperCase());
        }catch (IllegalArgumentException e){
            LoggingUtils.logError(this.getClass(),e.getClass(),e.getMessage());
            throw new InvalidParametersException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Unknown department has been provided.",
                    "assigned_to",
                    HttpParameters.REQUEST_BODY
            );
        }

        // validating the issue_id field,
        // must not contain any whitespace or null value
        String issueId = ticketRequest.getIssueId();
        if(io.micrometer.common.util.StringUtils.isBlank(issueId)
                || io.micrometer.common.util.StringUtils.isEmpty(issueId)
                || StringUtils.containsWhitespace(issueId))
            throw new InvalidParametersException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Contains whitespace or NULL / empty value.",
                    "issue_id",
                    HttpParameters.REQUEST_BODY
            );

        TicketDocument ticketDocument=new TicketDocument();
        Ticket ticket=new Ticket();
        ticket.setMessage(ticketRequest.getMessage());
        ticketDocument.setDepartmentId(department.name());
        ticketDocument.setTicket(ticket);
        ticketDocument.set_id(utilities.generateId());
        ticketDocument.setStatus(Status.TICKET_RAISED.name());
        ticketDocument.setPostedOn(System.currentTimeMillis());
        ticketDocument.setIssueId(issueId);

        String save = ticketDao.saveTicket(ticketDocument);
        String response = String.format("""
                {
                    "ticket_id": "%s"
                }
                """,save);
        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.put("Content-Type",List.of("application/json;charset=utf-8"));
        return new ResponseEntity<>(response,headers,HttpStatus.OK);
    }

    @GetMapping(params = {"dept","limit"})
    public ResponseEntity<Object> getTicketsByDepartment(
            @RequestParam(name = "dept") String deptId,
            @RequestParam(name = "limit") Integer limit,
            @RequestParam(name = "status", required = false,defaultValue = "TICKET_RAISED") String status,
            @RequestParam(name = "r_s",required = false,defaultValue = "0") Long postedOnOfStartRange,
            @RequestParam(name = "r_e",required = false) Long postedOnOfEndRange
    ){
        // checking if department value is valid
        if(StringUtils.containsWhitespace(deptId))
            throw new InvalidParametersException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Contains whitespace.",
                    "dept",
                    HttpParameters.QUERY
            );

        Department department;
        try{
            department = Department.valueOf(deptId.toUpperCase());
        }catch (IllegalArgumentException e){
            LoggingUtils.logError(this.getClass(), e.getClass(), e.getMessage());
            throw new InvalidParametersException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Unknown Department provided.",
                    "dept",
                    HttpParameters.QUERY
            );
        }

        // capping pagination to max limit set
        limit = Math.min(limit,MAX_PAGINATION_LIMIT);

        // setting a default value of status if NULL
        // checking is status has a valid value.
        try{
            status = status.toUpperCase();
            Status.valueOf(status);
        }catch (IllegalArgumentException e){
            LoggingUtils.logError(this.getClass(), e.getClass(), e.getMessage());
            throw new InvalidParametersException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Unknown Status provided.",
                    "status",
                    HttpParameters.QUERY
            );
        }

        // if r_s has negative value
        if(postedOnOfStartRange<0)
            throw new InvalidParametersException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Cannot be negative.",
                    "r_s",
                    HttpParameters.QUERY
            );


        // if r_e is not null then value must be greater than or equal to r_s query param
        if(postedOnOfEndRange!=null)
            postedOnOfEndRange = Math.max(postedOnOfEndRange,postedOnOfStartRange);

        // fetching required documents from database
        List<TicketDocument> ticketDocumentList =
                ticketDao.getTickets(department.name(),status,limit,postedOnOfStartRange,postedOnOfEndRange);

        // creating response
        int count = ticketDocumentList.size();
        GetTicketResponse response;
        if(count>0){
            response = new GetTicketResponse(
                    count,
                    ticketDocumentList,
                    ticketDocumentList.get(0).getPostedOn(),
                    ticketDocumentList.get(count-1).getPostedOn()
            );
        }
        else
            response = new GetTicketResponse();

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/{ticket-id}")
    public ResponseEntity<TicketDocument> getTicketByItsId(@PathVariable(name = "ticket-id") String ticketId){
        if(StringUtils.containsWhitespace(ticketId))
            throw new InvalidParametersException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Contains whitespace",
                    "{ticket-id}",
                    HttpParameters.PATH
            );
        TicketDocument ticketDocument = ticketDao.getTicketById(ticketId);
        if(ticketDocument==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ticketDocument,HttpStatus.OK);
    }

    @PatchMapping("/{ticket-id}/status")
    public ResponseEntity<Object> updateStatusFieldOfTicket(
            @PathVariable(name = "ticket-id") String ticketId,
            @RequestBody String newStatusJsonStr
    ) {
        try
        {
            String newStatus = new ObjectMapper().readTree(newStatusJsonStr).get("new_status").textValue();
            Status updatedStatus = Status.valueOf(newStatus);
            if(ticketDao.updateStatus(ticketId,updatedStatus)==true)
                return ResponseEntity.noContent().build();
            return ResponseEntity.internalServerError().build();
        }
        catch (JsonProcessingException e)
        {
            LoggingUtils.logError(this.getClass(),e.getClass(),e.getMessage());
            throw new InvalidParametersException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Invalid request body.",
                    "request.body",
                    HttpParameters.REQUEST_BODY
            );
        }
        catch (NullPointerException e)
        {
            LoggingUtils.logError(this.getClass(),e.getClass(),e.getMessage());
            throw new InvalidParametersException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Cannot find the required field.",
                    "request.body",
                    HttpParameters.REQUEST_BODY
            );
        }
        catch (IllegalArgumentException illegalStatusArgument)
        {
            LoggingUtils.logError(
                    this.getClass(),
                    illegalStatusArgument.getClass(),
                    illegalStatusArgument.getMessage()
            );
            throw new InvalidParametersException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Unknown Status provided",
                    "request.body.new_status",
                    HttpParameters.REQUEST_BODY
            );
        }
    }
}
