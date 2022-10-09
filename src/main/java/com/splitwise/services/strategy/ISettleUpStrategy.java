package com.splitwise.services.strategy;

import com.splitwise.dtos.SettleUpResponseDTO;
import com.splitwise.exception.GroupNotFoundException;
import com.splitwise.exception.UserNotFoundException;

public interface ISettleUpStrategy {
    SettleUpResponseDTO settleUp(Long userId) throws UserNotFoundException;
    SettleUpResponseDTO settleUpGroupExpense(Long userId, Long groupId) throws UserNotFoundException, GroupNotFoundException;
}
