package com.example.axonsagademo.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerService {
    public boolean isVerified(UUID customerId) {
        /*
            some business logic here
        */
        return true;
    }
}
