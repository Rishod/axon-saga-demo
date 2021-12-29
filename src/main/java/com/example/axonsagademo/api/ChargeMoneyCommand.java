package com.example.axonsagademo.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargeMoneyCommand {
    private UUID chargeId;
    private OrderItems items;
}
