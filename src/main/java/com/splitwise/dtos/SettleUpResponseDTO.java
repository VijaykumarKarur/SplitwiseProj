package com.splitwise.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SettleUpResponseDTO extends ResponseDTO{
    private List<Transaction> transactionList;
}
