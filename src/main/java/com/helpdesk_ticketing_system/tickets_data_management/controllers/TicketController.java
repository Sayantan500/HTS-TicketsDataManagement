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
        TicketDocument ticketDocument=new TicketDocument();
        Ticket ticket=new Ticket();
        ticket.setMessage(ticketRequest.getMessage());
        ticketDocument.setDepartmentId(ticketRequest.getDepartmentId());
        ticketDocument.setTicket(ticket);
        ticketDocument.set_id(utilities.generateId());
        ticketDocument.setStatus(Status.TICKET_RAISED.name());
        ticketDocument.setPostedOn(System.currentTimeMillis());
        ticketDocument.setIssueId(ticketRequest.getIssueId());

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
                ticketDao.getTickets(deptId,status,limit,postedOnOfStartRange,postedOnOfEndRange);

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

        return new ResponseEntity<>(response,HttpStatus.PARTIAL_CONTENT);
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
