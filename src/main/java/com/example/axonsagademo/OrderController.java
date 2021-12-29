package com.example.axonsagademo;

import com.example.axonsagademo.api.Order;
import com.example.axonsagademo.api.OrderCreatedEvent;
import com.example.axonsagademo.api.PlaceOrderRequest;
import com.example.axonsagademo.services.OrderService;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final EventGateway eventGateway;

    public OrderController(OrderService orderService, EventGateway eventGateway) {
        this.orderService = orderService;
        this.eventGateway = eventGateway;
    }

    @PostMapping("/order")
    public Order placeOrder(@RequestBody final PlaceOrderRequest request) {
        // Creating order with status PENDING
        final Order order = orderService.placeOrder(request);

        eventGateway.publish(OrderCreatedEvent.builder()
                .orderId(order.getId())
                .customerId(order.getCustomerId())
                .items(order.getItems())
                .build()
        );

        return order;
    }

}
