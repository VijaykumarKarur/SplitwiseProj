package com.splitwise.dtos;

import com.splitwise.models.Expense;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddExpenseResponseDTO extends ResponseDTO{
    Expense expense;
}
