package com.example.axonsagademo.services;

import com.example.axonsagademo.api.OrderItems;
import com.example.axonsagademo.api.Shipping;
import com.example.axonsagademo.api.ShippingStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ShippingService {
    public Optional<Shipping> findById(UUID shippingId) {
        return null;
    }

    public Shipping create(UUID shippingId, OrderItems orderItems) {
        return null;
    }

    public void rejectShipping(UUID shippingId) {

    }

    public void approveShipping(UUID shippingId) {

    }

    public ShippingStatus getCurrentStatus(UUID shippingId) {
        return null;
    }
}
