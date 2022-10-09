package com.splitwise.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Groups")
public class Group extends BaseModel{
    private String name;

    @ManyToOne
    private User admin;

    @ManyToMany
    private List<User> members;

    @OneToMany
    private List<Expense> expenses;
}
