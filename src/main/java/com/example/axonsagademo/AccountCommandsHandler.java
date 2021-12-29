package com.example.axonsagademo;

import com.example.axonsagademo.api.Charge;
import com.example.axonsagademo.api.ChargeMoneyCommand;
import com.example.axonsagademo.api.MoneyChargingFailedEvent;
import com.example.axonsagademo.api.MoneySuccessfullyChargedEvent;
import com.example.axonsagademo.services.AccountService;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;

@Service
public class AccountCommandsHandler {
    private final AccountService accountService;
    private final EventGateway eventGateway;

    public AccountCommandsHandler(AccountService accountService, EventGateway eventGateway) {
        this.accountService = accountService;
        this.eventGateway = eventGateway;
    }

    @CommandHandler
    public void handle(final ChargeMoneyCommand command) {
        try {
            final Charge charge = accountService.getChargeById(command.getChargeId())
                    .orElseGet(() -> accountService.mageCharge(command.getChargeId(), command.getItems()));

            eventGateway.publish(new MoneySuccessfullyChargedEvent(charge.getChargeId()));
        } catch (Exception e) {
            eventGateway.publish(new MoneyChargingFailedEvent(command.getChargeId()));
        }
    }
}
