package com.helpdesk_ticketing_system.tickets_data_management.utilities;

import java.util.logging.Logger;

public class LoggingUtils {
    public static void logError(Class<?> forClass, String message){
        Logger.getLogger(forClass.getName()).severe(message);
    }
}
