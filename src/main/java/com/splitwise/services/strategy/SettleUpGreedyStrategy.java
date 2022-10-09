package com.splitwise.services.strategy;

import com.splitwise.dtos.SettleUpResponseDTO;
import com.splitwise.dtos.Transaction;
import com.splitwise.dtos.UserDTO;
import com.splitwise.dtos.UserSettleUpAmt;
import com.splitwise.exception.GroupNotFoundException;
import com.splitwise.exception.UserNotFoundException;
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
public class SettleUpGreedyStrategy implements ISettleUpStrategy{
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Override
    @Transactional
    public SettleUpResponseDTO settleUp(Long userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException(userId);
        }

        Map<Long, UserSettleUpAmt> mapUserSettleAmt = new HashMap<>();
        List<Expense> userExpenses = expenseRepository.findExpensesByUser(user.get());
        for(Expense userExpense: userExpenses){
            for(User userPaid: userExpense.getPaidBy().keySet()){
                Long amt = userExpense.getPaidBy().get(userPaid);
                if(!mapUserSettleAmt.containsKey(userPaid.getId())){
                    mapUserSettleAmt.put(userPaid.getId(),
                            new UserSettleUpAmt(userPaid, amt));
                }
                else{
                    UserSettleUpAmt userSettleUpAmt = mapUserSettleAmt.get(userPaid.getId());
                    userSettleUpAmt.setAmount(userSettleUpAmt.getAmount() + amt);
                    mapUserSettleAmt.put(userPaid.getId(), userSettleUpAmt);
                }
            }

            for(User userOwed: userExpense.getOwedBy().keySet()){
                Long amt = userExpense.getOwedBy().get(userOwed);
                if(!mapUserSettleAmt.containsKey(userOwed.getId())){
                    mapUserSettleAmt.put(userOwed.getId(),
                            new UserSettleUpAmt(userOwed, amt));
                }
                else{
                    UserSettleUpAmt userSettleUpAmt = mapUserSettleAmt.get(userOwed.getId());
                    userSettleUpAmt.setAmount(userSettleUpAmt.getAmount() - amt);
                    mapUserSettleAmt.put(userOwed.getId(), userSettleUpAmt);
                }
            }
        }

        List<Transaction> transactionList = getTransactionsFromUserSettleAmt(mapUserSettleAmt);
        SettleUpResponseDTO settleUpResponseDTO = new SettleUpResponseDTO();
        settleUpResponseDTO.setTransactionList(transactionList);
        return settleUpResponseDTO;
    }

    private List<Transaction> getTransactionsFromUserSettleAmt(Map<Long, UserSettleUpAmt> mapUserSettleAmt) {
        List<Long> userIdList = mapUserSettleAmt.keySet().stream().toList();
        List<Transaction> transactions = new ArrayList<>();

        for(int index = 0; index < userIdList.size() - 1; index++){
            Long currentUserId = userIdList.get(index);
            Long nextUserId = userIdList.get(index + 1);
            UserSettleUpAmt currentUser = mapUserSettleAmt.get(currentUserId);
            UserSettleUpAmt nextUser = mapUserSettleAmt.get(nextUserId);
            Long amount = currentUser.getAmount();
            if(amount > 0L){
                Transaction transaction = Transaction
                        .builder()
                        .fromUser(UserDTO.from(nextUser.getUser()))
                        .toUser(UserDTO.from(currentUser.getUser()))
                        .amount(amount)
                        .build();
                transactions.add(transaction);
                currentUser.setAmount(0L);
                mapUserSettleAmt.put(currentUserId, currentUser);
                nextUser.setAmount(nextUser.getAmount() + amount);
            }
            else if(amount < 0L){
                Transaction transaction = Transaction
                        .builder()
                        .fromUser(UserDTO.from(currentUser.getUser()))
                        .toUser(UserDTO.from(nextUser.getUser()))
                        .amount(-amount)
                        .build();
                transactions.add(transaction);
                currentUser.setAmount(0L);
                mapUserSettleAmt.put(currentUserId,currentUser);
                nextUser.setAmount(nextUser.getAmount() - amount);
                mapUserSettleAmt.put(nextUserId, nextUser);
            }
        }

        return transactions;
    }

    @Override
    @Transactional
    public SettleUpResponseDTO settleUpGroupExpense(Long userId, Long groupId) throws UserNotFoundException, GroupNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException(userId);
        }

        Optional<Group> group = groupRepository.findById(groupId);
        if(group.isEmpty()){
            throw new GroupNotFoundException(groupId);
        }

        List<Expense> expenses = group.get().getExpenses();

        Map<Long, UserSettleUpAmt> mapUserSettleAmt = new HashMap<>();

        expenses = getUserExpensesFromExpenses(expenses, userId);
        for(Expense userExpense: expenses){
            for(User userPaid: userExpense.getPaidBy().keySet()){
                Long amt = userExpense.getPaidBy().get(userPaid);
                if(!mapUserSettleAmt.containsKey(userPaid.getId())){
                    mapUserSettleAmt.put(userPaid.getId(),
                            new UserSettleUpAmt(userPaid, amt));
                }
                else{
                    UserSettleUpAmt userSettleUpAmt = mapUserSettleAmt.get(userPaid.getId());
                    userSettleUpAmt.setAmount(userSettleUpAmt.getAmount() + amt);
                    mapUserSettleAmt.put(userPaid.getId(), userSettleUpAmt);
                }
            }

            for(User userOwed: userExpense.getOwedBy().keySet()){
                Long amt = userExpense.getOwedBy().get(userOwed);
                if(!mapUserSettleAmt.containsKey(userOwed.getId())){
                    mapUserSettleAmt.put(userOwed.getId(),
                            new UserSettleUpAmt(userOwed, amt));
                }
                else{
                    UserSettleUpAmt userSettleUpAmt = mapUserSettleAmt.get(userOwed.getId());
                    userSettleUpAmt.setAmount(userSettleUpAmt.getAmount() - amt);
                    mapUserSettleAmt.put(userOwed.getId(), userSettleUpAmt);
                }
            }
        }

        List<Transaction> transactionList = getTransactionsFromUserSettleAmt(mapUserSettleAmt);
        SettleUpResponseDTO settleUpResponseDTO = new SettleUpResponseDTO();
        settleUpResponseDTO.setTransactionList(transactionList);
        return settleUpResponseDTO;
    }

    public List<Expense> getUserExpensesFromExpenses(List<Expense> expenses, Long userId){
        List<Expense> userExpenses = new ArrayList<>();
        for(Expense expense : expenses){
            boolean expenseAdded = false;
            for(User user : expense.getOwedBy().keySet()){
                if(user.getId().equals(userId)){
                    userExpenses.add(expense);
                    expenseAdded = true;
                    break;
                }
            }
            if(!expenseAdded){
                for(User user : expense.getPaidBy().keySet()){
                    if(user.getId().equals(userId)){
                        userExpenses.add(expense);
                        break;
                    }
                }
            }
        }

        return userExpenses;
    }
}
