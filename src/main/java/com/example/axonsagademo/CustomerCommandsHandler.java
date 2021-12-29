package com.example.axonsagademo;

import com.example.axonsagademo.api.CustomerVerificationFailedEvent;
import com.example.axonsagademo.api.CustomerVerifiedEvent;
import com.example.axonsagademo.api.VerifyCustomerCommand;
import com.example.axonsagademo.services.CustomerService;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;

@Service
public class CustomerCommandsHandler {
    private final CustomerService customerService;
    private final EventGateway eventGateway;

    public CustomerCommandsHandler(CustomerService customerService, EventGateway eventGateway) {
        this.customerService = customerService;
        this.eventGateway = eventGateway;
    }

    @CommandHandler
    public void handle(final VerifyCustomerCommand command) {
        if (customerService.isVerified(command.getCustomerId())) {
            eventGateway.publish(new CustomerVerifiedEvent(command.getOrderId()));
        } else {
            eventGateway.publish(new CustomerVerificationFailedEvent(command.getOrderId()));
        }
    }
}
