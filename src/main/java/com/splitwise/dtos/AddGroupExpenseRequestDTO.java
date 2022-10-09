package com.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddGroupExpenseRequestDTO extends AuthenticatedRequestDTO{
    private Long createdBy;
    private String description;
    private Long groupId;
    private List<Long> amounts;
    private ExpenseSplit expenseSplit;
    private PaidBy paidBy;
    private List<Long> split;
}
