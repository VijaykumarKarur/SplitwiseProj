package com.splitwise.services;

import com.splitwise.dtos.*;
import com.splitwise.exception.InvalidParameterException;
import com.splitwise.exception.UserNotFoundException;
import com.splitwise.models.*;
import com.splitwise.models.Currency;
import com.splitwise.repositories.ExpenseRepository;
import com.splitwise.repositories.GroupRepository;
import com.splitwise.repositories.UserRepository;
import com.splitwise.services.passwordencoder.IPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IPasswordEncoder passwordEncoder;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private GroupRepository groupRepository;

    public User registerUser(String name, String phoneNo, String password){
        String encodedPassword = passwordEncoder.encodePassword(password);

        User user = new User();
        user.setName(name);
        user.setHashedPassword(encodedPassword);
        user.setPhoneNo(phoneNo);

        return userRepository.save(user);
    }

    public UpdateProfileResponseDTO updateUser(UpdateProfileRequestDTO requestDTO) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(requestDTO.getId());
        if(userOptional.isEmpty()){
            throw new UserNotFoundException(requestDTO.getId());
        }
        User user = userOptional.get();
        if(Objects.nonNull(requestDTO.getName()) && !"".equals(requestDTO.getName())){
            user.setName(requestDTO.getName());
        }
        if(Objects.nonNull(requestDTO.getPassword()) && !"".equals(requestDTO.getPassword())){
            user.setHashedPassword(passwordEncoder.encodePassword(requestDTO.getPassword()));
        }
        if(Objects.nonNull(requestDTO.getPhoneNo()) && !"".equals(requestDTO.getPhoneNo())){
            user.setPhoneNo(requestDTO.getPhoneNo());
        }

        return UpdateProfileResponseDTO
                .builder()
                .user(userRepository.save(user))
                .build();
    }


    public Long getMyTotal(Long userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException(userId);
        }
        List<Expense> expenses = expenseRepository.findExpensesPaidBy(user.get());
        Long amount = 0L;
        for(Expense expense : expenses) {
            for (Map.Entry<User, Long> entry : expense.getPaidBy().entrySet()) {
                if (entry.getKey().getId().equals(userId)) {
                    amount += entry.getValue();
                }
            }
        }
        expenses = expenseRepository.findExpensesOwedBy(user.get());
        for(Expense expense : expenses) {
            for(Map.Entry<User, Long> entry : expense.getOwedBy().entrySet()){
                if(entry.getKey().getId().equals(userId)){
                    amount -= entry.getValue();
                }
            }
        }
        return amount;
    }

    public List<Group> getGroupsById(Long userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException(userId);
        }
        return groupRepository.findAllByMembers_Id(userId);
    }

    public List<UserExpenseHistory> getExpensesById(Long userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException(userId);
        }

        List<UserExpenseHistory> userExpenseHistories = new ArrayList<>();
        List<Expense> expensesOwedByList = expenseRepository.findExpensesOwedBy(user.get());
        List<Expense> expensesPaidByList = expenseRepository.findExpensesPaidBy(user.get());
        for(Expense expense : expensesOwedByList){
            for(User userOwed : expense.getOwedBy().keySet()){
                if(userOwed.getId().equals(user.get().getId())){
                    userExpenseHistories.add(UserExpenseHistory
                            .builder()
                                    .description(expense.getDescription())
                                    .paymentStatus(PaymentStatus.OWES)
                                    .amount(expense.getOwedBy().get(userOwed))
                            .build());
                }
            }
        }

        for(Expense expense : expensesPaidByList){
            for(User userPaid : expense.getPaidBy().keySet()){
                if(userPaid.getId().equals(user.get().getId())){
                    userExpenseHistories.add(UserExpenseHistory
                            .builder()
                            .description(expense.getDescription())
                            .paymentStatus(PaymentStatus.PAID)
                            .amount(expense.getPaidBy().get(userPaid))
                            .build());
                }
            }
        }

        return userExpenseHistories;
    }

    public Map<User, Long> getPaidBy(List<Long> participants, List<Long> amounts, PaidBy paidBy) throws UserNotFoundException {
        Map<User, Long> mapPaidBy = new HashMap<>();
        if(paidBy.equals(PaidBy.IPAY)){
            Optional<User> user = userRepository.findById(participants.get(0));
            if(user.isEmpty()){
                throw new UserNotFoundException(participants.get(0));
            }
            mapPaidBy.put(user.get(), amounts.get(0));
            return mapPaidBy;
        }
        int index = 0;
        for(Long participant : participants){
            Optional<User> user = userRepository.findById(participant);
            if(user.isEmpty()){
                throw new UserNotFoundException(participant);
            }
            Long amount = amounts.get(index);
            index++;
            mapPaidBy.put(user.get(), amount);
        }
        return mapPaidBy;
    }

    public Map<User, Long> getOwedBy(List<Long> participants, List<Long> split, ExpenseSplit expenseSplit, Long amount) throws UserNotFoundException, InvalidParameterException {
        Map<User, Long> owedBy = new HashMap<>();
        int index = 0;
        Long sumAmount = 0L;
        Long sumSplit = 0L;
        for(Long s : split){
            sumSplit += s;
        }
        if(expenseSplit.equals(ExpenseSplit.PERCENT)){
            if(sumSplit != 100){
                throw new InvalidParameterException("splits do not add upto 100%");
            }
        }
        if(expenseSplit.equals(ExpenseSplit.EXACT)){
            if(!sumSplit.equals(amount)){
                throw new InvalidParameterException("splits do not add upto "+ amount);
            }
        }
        for(Long participant : participants){
            Optional<User> user = userRepository.findById(participant);
            if(user.isEmpty()){
                throw new UserNotFoundException(participant);
            }
            Long userAmount = 0L;
            switch (expenseSplit){
                case PERCENT:
                    if(index < participants.size() - 1){
                        userAmount = (long)Math.round(split.get(index) * amount / 100);
                        sumAmount += userAmount;
                    }
                    else{
                        userAmount = amount - sumAmount;
                    }
                    break;
                case RATIO:
                    if(index < participants.size() - 1){
                        userAmount = (long)Math.round(split.get(index) * amount / sumSplit);
                        sumAmount += userAmount;
                    }
                    else{
                        userAmount = amount - sumAmount;
                    }
                    break;
                case EXACT:
                    userAmount = split.get(index);
                    break;
                case EQUAL:
                    if(index < participants.size() - 1){
                        userAmount = (long)Math.round(amount / participants.size());
                        sumAmount += userAmount;
                    }
                    else{
                        userAmount = amount - sumAmount;
                    }
                    break;
            }
            owedBy.put(user.get(), userAmount);
        }
        return owedBy;
    }

    public Expense addExpense(Long createdById, String description,
                        List<Long> amounts, ExpenseSplit expenseSplit,
                           List<Long> split, List<Long> participants, PaidBy paidBy) throws UserNotFoundException, InvalidParameterException {

        Optional<User> createdBy = userRepository.findById(createdById);
        if(createdBy.isEmpty()){
            throw new UserNotFoundException(createdById);
        }
        Long amount = 0L;
        for(Long amt : amounts){
            amount += amt;
        }

        Map<User, Long> mapPaidBy = getPaidBy(participants, amounts, paidBy);

        Map<User, Long> owedBy = getOwedBy(participants, split, expenseSplit, amount);

        Expense expense = new Expense();
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setCreatedBy(createdBy.get());
        expense.setCurrency(Currency.IND);
        expense.setPaidBy(mapPaidBy);
        expense.setOwedBy(owedBy);

        return expenseRepository.save(expense);

    }

}
