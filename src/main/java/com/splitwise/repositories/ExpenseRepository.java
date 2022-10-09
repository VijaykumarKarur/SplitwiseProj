package com.splitwise.repositories;

import com.splitwise.models.Expense;
import com.splitwise.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query(value = "select E from Expense E join E.owedBy O where (KEY(O) = ?1)")
    List<Expense> findExpensesOwedBy(User user);

    @Query(value = "select E from Expense E join E.paidBy P where (KEY(P) = ?1)")
    List<Expense> findExpensesPaidBy(User user);

    @Query(value = "select DISTINCT E " +
            "       from Expense E join E.owedBy O join E.paidBy P" +
            "       where KEY(P) = ?1 OR KEY(O) = ?1")
    List<Expense> findExpensesByUser(User user);
}
