package com.helpdesk_ticketing_system.tickets_data_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class TicketsDataManagementApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(TicketsDataManagementApplication.class);
        application.setDefaultProperties(Collections
                .singletonMap("server.port", System.getenv("tomcat_server_port")));
        application.run(args);
    }

}