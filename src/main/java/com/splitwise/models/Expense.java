package com.splitwise.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.Map;

@Getter
@Setter
@Entity
public class Expense extends BaseModel{
    private String description;

    @ManyToOne
    private User createdBy;

    private Long amount;

    private Currency currency;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<User, Long> paidBy;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<User, Long> owedBy;
}
