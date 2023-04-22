package com.helpdesk_ticketing_system.tickets_data_management.exceptions_handling.exceptions;

import com.helpdesk_ticketing_system.tickets_data_management.entities.HttpParameters;

public class InvalidParametersException extends RuntimeException
{
    private final Integer statusCode;
    private final String paramName;
    private final HttpParameters parameterType;

    public InvalidParametersException(Integer statusCode,String reason, String paramName, HttpParameters parameterType) {
        super(reason);
        this.statusCode = statusCode;
        this.paramName = paramName;
        this.parameterType = parameterType;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public HttpParameters getParameterType() {
        return parameterType;
    }

    public String getParamName() {
        return paramName;
    }
}
