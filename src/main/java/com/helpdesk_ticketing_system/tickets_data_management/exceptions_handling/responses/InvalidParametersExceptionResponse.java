package com.helpdesk_ticketing_system.tickets_data_management.exceptions_handling.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.helpdesk_ticketing_system.tickets_data_management.entities.HttpParameters;

public class InvalidParametersExceptionResponse {
    @JsonProperty("status")
    private final Integer statusCode;
    @JsonProperty("param-type")
    private final HttpParameters param_type;
    @JsonProperty("parameter")
    private final String message;
    @JsonProperty("reason")
    private final String reason;

    public InvalidParametersExceptionResponse(Integer statusCode,HttpParameters param_type, String message, String reason) {
        this.statusCode = statusCode;
        this.param_type = param_type;
        this.message = message;
        this.reason = reason;
    }
}
