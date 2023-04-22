package com.helpdesk_ticketing_system.tickets_data_management.exceptions_handling.handlers;

import com.helpdesk_ticketing_system.tickets_data_management.exceptions_handling.exceptions.InvalidParametersException;
import com.helpdesk_ticketing_system.tickets_data_management.exceptions_handling.responses.InvalidParametersExceptionResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RequestExceptionHandlers {
    @ExceptionHandler
    public ResponseEntity<InvalidParametersExceptionResponse> handleInvalidQueryParamsException(
            InvalidParametersException invalidParametersException){
        InvalidParametersExceptionResponse response = new InvalidParametersExceptionResponse(
                invalidParametersException.getStatusCode(),
                invalidParametersException.getParameterType(),
                invalidParametersException.getParamName(),
                invalidParametersException.getMessage()
        );

        return new ResponseEntity<>(
                response,
                HttpStatusCode.valueOf(invalidParametersException.getStatusCode())
        );
    }
}
