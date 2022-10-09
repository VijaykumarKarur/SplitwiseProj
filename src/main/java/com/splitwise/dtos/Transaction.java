package com.splitwise.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    private UserDTO fromUser;
    private UserDTO toUser;
    private Long amount;
}
