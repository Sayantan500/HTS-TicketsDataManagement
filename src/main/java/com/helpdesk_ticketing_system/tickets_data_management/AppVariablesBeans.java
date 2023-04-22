package com.helpdesk_ticketing_system.tickets_data_management;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Calendar;
import java.util.Random;

@Configuration
public class AppVariablesBeans {
    @Bean("max_character_limit_for_message")
    public Integer maxCharLimitForMsg(){
        return Integer.parseInt(System.getenv("max_character_limit_for_message"));
    }

    @Bean
    public String solutionIdPrefix(){
        return System.getenv("solution_id_prefix");
    }

    @Bean
    public Calendar calendar(){
        return Calendar.getInstance();
    }

    @Bean
    public Random random(){
        return new Random();
    }

    @Bean
    public Integer randomNumberRange(){
        return Integer.parseInt(System.getenv("solutionId_random_number_range"));
    }

    @Bean("digitsToExtract")
    public Integer digitsToExtract(){
        return Integer.parseInt(System.getenv("solutionId_digits_to_extract"));
    }
}
