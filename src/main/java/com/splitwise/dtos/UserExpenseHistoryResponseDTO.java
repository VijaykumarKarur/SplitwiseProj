package com.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserExpenseHistoryResponseDTO extends ResponseDTO{
    private Long userId;
    private List<UserExpenseHistory> userExpenseHistories;
}
