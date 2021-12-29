package com.example.axonsagademo;

import com.example.axonsagademo.api.ApproveShippingCommand;
import com.example.axonsagademo.api.CreateShippingCommand;
import com.example.axonsagademo.api.CustomerVerificationFailedEvent;
import com.example.axonsagademo.api.CustomerVerifiedEvent;
import com.example.axonsagademo.api.MoneyChargingFailedEvent;
import com.example.axonsagademo.api.MoneySuccessfullyChargedEvent;
import com.example.axonsagademo.api.OrderCreatedEvent;
import com.example.axonsagademo.api.OrderItems;
import com.example.axonsagademo.api.ChargeMoneyCommand;
import com.example.axonsagademo.api.RejectShippingCommand;
import com.example.axonsagademo.api.ShippingApprovedEvent;
import com.example.axonsagademo.api.ShippingCreatedEvent;
import com.example.axonsagademo.api.ShippingRejectedEvent;
import com.example.axonsagademo.api.VerifyCustomerCommand;
import com.example.axonsagademo.services.OrderService;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
public class OrderSagaOrchestrator {

    private UUID orderId;
    private UUID shippingId;
    private UUID chargeId;
    private OrderItems items;

    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient OrderService orderService;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(final OrderCreatedEvent event) {
        this.orderId = event.getOrderId();
        this.items = event.getItems();

        commandGateway.send(new VerifyCustomerCommand(event.getOrderId(), event.getCustomerId()));
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(final CustomerVerifiedEvent event) {
        this.shippingId = this.shippingId != null ? this.shippingId : UUID.randomUUID();
        SagaLifecycle.associateWith("shippingId", shippingId.toString());

        commandGateway.send(new CreateShippingCommand(shippingId, this.items));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(final CustomerVerificationFailedEvent event) {
        orderService.rejectOrder(this.orderId);
    }

    @SagaEventHandler(associationProperty = "shippingId")
    public void handle(final ShippingCreatedEvent event) {
        this.chargeId = this.chargeId != null ? this.chargeId : UUID.randomUUID();
        SagaLifecycle.associateWith("chargeId", chargeId.toString());

        commandGateway.send(new ChargeMoneyCommand(this.chargeId, this.items));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "shippingId")
    public void handle(final ShippingRejectedEvent event) {
        orderService.rejectOrder(this.orderId);
    }

    @SagaEventHandler(associationProperty = "chargeId")
    public void handle(final MoneySuccessfullyChargedEvent event) {
        commandGateway.send(new ApproveShippingCommand(this.shippingId));
    }

    @SagaEventHandler(associationProperty = "chargeId")
    public void handle(final MoneyChargingFailedEvent event) {
        commandGateway.send(new RejectShippingCommand(this.shippingId));
    }

    @SagaEventHandler(associationProperty = "shippingId")
    public void handle(final ShippingApprovedEvent event) {
        orderService.approveOrder(this.orderId);
    }
}
