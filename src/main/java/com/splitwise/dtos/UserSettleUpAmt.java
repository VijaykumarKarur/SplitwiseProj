package com.splitwise.dtos;

import com.splitwise.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettleUpAmt {
    private User user;
    private Long amount;
}
