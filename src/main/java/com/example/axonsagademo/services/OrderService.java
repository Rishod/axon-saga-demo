package com.example.axonsagademo.services;

import com.example.axonsagademo.api.Order;
import com.example.axonsagademo.api.PlaceOrderRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    public Order placeOrder(final PlaceOrderRequest request) {
        /*
            some business logic here
        */

        return new Order();
    }

    public void rejectOrder(UUID orderId) {
        /*
            some business logic here
        */
    }

    public void approveOrder(UUID orderId) {
        /*
            some business logic here
        */
    }
}
