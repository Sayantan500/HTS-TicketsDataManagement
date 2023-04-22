package com.helpdesk_ticketing_system.tickets_data_management;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppVariablesConfig
{
    @Bean("max_pagination_limit")
    public Integer maxPaginationLimit(){
        return Integer.parseInt(System.getenv("pagination_limit"));
    }
}
