package com.example.axonsagademo.services;

import com.example.axonsagademo.api.Charge;
import com.example.axonsagademo.api.OrderItems;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {
    public Optional<Charge> getChargeById(UUID chargeId) {
        return Optional.empty();
    }

    public Charge mageCharge(UUID chargeId, OrderItems items) {
        return null;
    }
}
