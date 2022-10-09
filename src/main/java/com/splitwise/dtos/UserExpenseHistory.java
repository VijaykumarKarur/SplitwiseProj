package com.splitwise.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserExpenseHistory {
    private PaymentStatus paymentStatus;
    private String description;
    private Long amount;
}
