package com.example.axonsagademo;

import com.example.axonsagademo.api.ApproveShippingCommand;
import com.example.axonsagademo.api.CreateShippingCommand;
import com.example.axonsagademo.api.RejectShippingCommand;
import com.example.axonsagademo.api.ShippingApprovedEvent;
import com.example.axonsagademo.api.ShippingCreatedEvent;
import com.example.axonsagademo.api.ShippingRejectedEvent;
import com.example.axonsagademo.api.ShippingStatus;
import com.example.axonsagademo.services.ShippingService;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;

@Service
public class ShippingCommandsHandler {
    private final ShippingService shippingService;
    private final EventGateway eventGateway;

    public ShippingCommandsHandler(ShippingService shippingService, EventGateway eventGateway) {
        this.shippingService = shippingService;
        this.eventGateway = eventGateway;
    }

    @CommandHandler
    public void handle(final CreateShippingCommand command) {
        // Handlers should always be idempotent!
        try {
            shippingService.findById(command.getShippingId())
                    .orElseGet(() -> shippingService.create(command.getShippingId(), command.getOrderItems()));

            eventGateway.publish(new ShippingCreatedEvent(command.getShippingId()));
        } catch (final Exception e) {
            eventGateway.publish(new ShippingRejectedEvent(command.getShippingId()));
        }
    }

    @CommandHandler
    public void handle(final ApproveShippingCommand command) {
        final ShippingStatus status = shippingService.getCurrentStatus(command.getShippingId());

        // This fix isolation problem (Semantic blocking)
        if (ShippingStatus.APPROVAL_PENDING.equals(status)) {
            shippingService.approveShipping(command.getShippingId());

            eventGateway.publish(new ShippingApprovedEvent(command.getShippingId()));
        }
    }

    @CommandHandler
    public void handle(final RejectShippingCommand command) {
        final ShippingStatus status = shippingService.getCurrentStatus(command.getShippingId());

        // This fix isolation problem (Semantic blocking)
        if (ShippingStatus.APPROVAL_PENDING.equals(status)) {
            shippingService.rejectShipping(command.getShippingId());

            eventGateway.publish(new ShippingRejectedEvent(command.getShippingId()));
        }
    }
}
