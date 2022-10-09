package com.splitwise.controller;

import com.splitwise.dtos.*;
import com.splitwise.exception.InvalidParameterException;
import com.splitwise.exception.UserNotFoundException;
import com.splitwise.models.Expense;
import com.splitwise.models.Group;
import com.splitwise.services.UserService;
import com.splitwise.services.strategy.ISettleUpStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import com.splitwise.models.User;
import java.util.List;

@ComponentScan(basePackages = "com.splitwise.services")
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ISettleUpStrategy settleUpStrategy;
    public RegisterUserResponseDTO registerUser(RegisterUserRequestDTO request){
        User user = userService.registerUser(request.getName(), request.getPhoneNo(), request.getPassword());

        RegisterUserResponseDTO response = new RegisterUserResponseDTO();
        response.setUser(user);
        response.setRequestId(1L);
        response.setStatus(Status.SUCCESS);

        return response;
    }

    public UpdateProfileResponseDTO updateUser(UpdateProfileRequestDTO requestDTO) throws UserNotFoundException {
        return userService.updateUser(requestDTO);
    }

    public MyTotalResponseDTO getMyTotal(MyTotalRequestDTO request) throws UserNotFoundException {
        Long amount = userService.getMyTotal(request.getId());

        MyTotalResponseDTO response = new MyTotalResponseDTO();
        response.setAmount(amount);
        response.setStatus(Status.SUCCESS);
        return response;
    }

    public UserGroupsResponseDTO getGroupsById(UserGroupsRequestDTO request) throws UserNotFoundException {
        List<Group> groups = userService.getGroupsById(request.getId());

        UserGroupsResponseDTO response = new UserGroupsResponseDTO();
        response.setGroups(groups);
        response.setStatus(Status.SUCCESS);
        return response;
    }

    public UserExpenseHistoryResponseDTO getExpensesById(UserExpenseHistoryRequestDTO request) throws UserNotFoundException {
        List<UserExpenseHistory> userExpenseHistories = userService.getExpensesById(request.getId());

        UserExpenseHistoryResponseDTO response = new UserExpenseHistoryResponseDTO();
        response.setUserId(request.getId());
        response.setUserExpenseHistories(userExpenseHistories);
        response.setStatus(Status.SUCCESS);

        return response;
    }

    public AddExpenseResponseDTO addExpense(AddExpenseRequestDTO request) throws UserNotFoundException, InvalidParameterException {
        Expense expense = userService.addExpense(request.getCreatedBy(),
                request.getDescription(),
                request.getAmounts(),
                request.getExpenseSplit(),
                request.getSplit(),
                request.getParticipants(),
                request.getPaidBy());

        AddExpenseResponseDTO response = new AddExpenseResponseDTO();
        response.setExpense(expense);
        response.setStatus(Status.SUCCESS);

        return response;
    }

    public SettleUpResponseDTO settleUp(Long userId) throws UserNotFoundException {
        SettleUpResponseDTO settleUpResponseDTO = settleUpStrategy.settleUp(userId);
        settleUpResponseDTO.setStatus(Status.SUCCESS);
        return settleUpResponseDTO;
    }

}
