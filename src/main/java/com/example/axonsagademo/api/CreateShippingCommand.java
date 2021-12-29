package com.example.axonsagademo.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateShippingCommand {
    private UUID shippingId;
    private OrderItems orderItems;
}
