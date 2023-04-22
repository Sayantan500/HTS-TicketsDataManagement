package com.helpdesk_ticketing_system.tickets_data_management.utilities;

import java.util.logging.Logger;

public class LoggingUtils {
    public static void logError(Class<?> errorThrownIn, Class<?> errorType, String errorMessage){
        String logMessage = String.format(
                "Error-Type : %s ----- Error Message : %s",
                errorType.getName(),
                errorMessage
        );
        Logger.getLogger(errorThrownIn.getName()).severe(logMessage);
    }
}
