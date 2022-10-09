package com.splitwise.services;

import com.splitwise.dtos.ExpenseSplit;
import com.splitwise.dtos.PaidBy;
import com.splitwise.exception.GroupNotFoundException;
import com.splitwise.exception.InvalidGroupAdminException;
import com.splitwise.exception.InvalidParameterException;
import com.splitwise.exception.UserNotFoundException;
import com.splitwise.models.Currency;
import com.splitwise.models.Expense;
import com.splitwise.models.Group;
import com.splitwise.models.User;
import com.splitwise.repositories.ExpenseRepository;
import com.splitwise.repositories.GroupRepository;
import com.splitwise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserService userService;

    public Group addGroup(Long userId, String name) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException(userId);
        }
        Group group = new Group();
        group.setAdmin(user.get());
        List<User> members = group.getMembers();
        if(members == null){
            members = new ArrayList<>();
        }
        members.add(user.get());
        group.setMembers(members);
        group.setName(name);

        return groupRepository.save(group);
    }

    @Transactional
    public Group addMember(Long adminId, Long groupId, Long memberId) throws GroupNotFoundException, UserNotFoundException, InvalidGroupAdminException {
        Optional<Group> group = groupRepository.findById(groupId);
        if(group.isEmpty()){
            throw new GroupNotFoundException(groupId);
        }
        Optional<User> admin = userRepository.findById(adminId);
        if(admin.isEmpty()){
            throw new UserNotFoundException(adminId);
        }
        if(!group.get().getAdmin().getId().equals(admin.get().getId())){
            throw new InvalidGroupAdminException(adminId, groupId);
        }
        Optional<User> user = userRepository.findById(memberId);
        if(user.isEmpty()){
            throw new UserNotFoundException(memberId);
        }
        List<User> members = group.get().getMembers();
        if(members == null){
            members = new ArrayList<>();
        }
        members.add(user.get());
        group.get().setMembers(members);
        return groupRepository.save(group.get());
    }

    @Transactional
    public Group addGroupExpense(Long createdById, String description, Long groupId,
                                        List<Long> amounts, ExpenseSplit expenseSplit,
                                        List<Long> split, PaidBy paidBy) throws UserNotFoundException, InvalidParameterException, GroupNotFoundException {

        Optional<User> createdBy = userRepository.findById(createdById);
        if(createdBy.isEmpty()){
            throw new UserNotFoundException(createdById);
        }

        Optional<Group> group = groupRepository.findById(groupId);
        if(group.isEmpty()){
            throw new GroupNotFoundException(groupId);
        }

        List<Long> participants = new ArrayList<>();
        for(User user : group.get().getMembers()){
            participants.add(user.getId());
        }

        Long amount = 0L;
        for(Long amt : amounts){
            amount += amt;
        }

        Map<User, Long> mapPaidBy = userService.getPaidBy(participants, amounts, paidBy);

        Map<User, Long> owedBy = userService.getOwedBy(participants, split, expenseSplit, amount);

        Expense expense = new Expense();
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setCreatedBy(createdBy.get());
        expense.setCurrency(Currency.IND);
        expense.setPaidBy(mapPaidBy);
        expense.setOwedBy(owedBy);


        Expense expenseCreated = expenseRepository.save(expense);
        if(group.get().getExpenses() == null){
            group.get().setExpenses(new ArrayList<>());
        }
        List<Expense> expenseList = group.get().getExpenses();
        expenseList.add(expenseCreated);
        group.get().setExpenses(expenseList);
        return groupRepository.save(group.get());
    }

}