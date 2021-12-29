package com.example.axonsagademo.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private UUID id = UUID.randomUUID();
    private UUID customerId = UUID.randomUUID();
    private OrderItems items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
