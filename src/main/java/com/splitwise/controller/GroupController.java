package com.splitwise.controller;

import com.splitwise.dtos.*;
import com.splitwise.exception.GroupNotFoundException;
import com.splitwise.exception.InvalidGroupAdminException;
import com.splitwise.exception.InvalidParameterException;
import com.splitwise.exception.UserNotFoundException;
import com.splitwise.models.Group;
import com.splitwise.services.GroupService;
import com.splitwise.services.strategy.ISettleUpStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GroupController {
    @Autowired
    private GroupService groupService;

    @Autowired
    private ISettleUpStrategy settleUpStrategy;

    public AddGroupResponseDTO addGroup(AddGroupRequestDTO request) throws UserNotFoundException {
        Group group = groupService.addGroup(request.getId(), request.getName());

        AddGroupResponseDTO response = new AddGroupResponseDTO();
        response.setGroup(group);
        response.setStatus(Status.SUCCESS);
        return response;
    }

    public AddMemberResponseDTO addMember(AddMemberRequestDTO request) throws UserNotFoundException, GroupNotFoundException, InvalidGroupAdminException {
        Group group = groupService.addMember(request.getId(), request.getGroupId(), request.getMemberId());

        AddMemberResponseDTO response = new AddMemberResponseDTO();
        response.setGroup(group);
        response.setStatus(Status.SUCCESS);
        return response;
    }

    public AddGroupExpenseResponseDTO addGroupExpense(AddGroupExpenseRequestDTO requestDTO) throws UserNotFoundException, InvalidParameterException, GroupNotFoundException {

        Group groupExpense = groupService.addGroupExpense(requestDTO.getCreatedBy(),
                requestDTO.getDescription(),
                requestDTO.getGroupId(),
                requestDTO.getAmounts(),
                requestDTO.getExpenseSplit(),
                requestDTO.getSplit(),
                requestDTO.getPaidBy());

        AddGroupExpenseResponseDTO response = new AddGroupExpenseResponseDTO();
        response.setGroup(groupExpense);
        response.setStatus(Status.SUCCESS);

        return response;
    }

    public SettleUpResponseDTO settleUpGroupExpense(Long userId, Long groupId) throws UserNotFoundException, GroupNotFoundException {
        SettleUpResponseDTO settleUpResponseDTO = settleUpStrategy.settleUpGroupExpense(userId, groupId);
        settleUpResponseDTO.setStatus(Status.SUCCESS);
        return settleUpResponseDTO;
    }

}
