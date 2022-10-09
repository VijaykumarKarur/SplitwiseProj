package com.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddExpenseRequestDTO extends AuthenticatedRequestDTO{
    private Long createdBy;
    private String description;
    private List<Long> amounts;
    private ExpenseSplit expenseSplit;
    private PaidBy paidBy;
    private List<Long> split;
    private List<Long> participants;
}
