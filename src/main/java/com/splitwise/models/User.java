package com.splitwise.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "Users")
public class User extends BaseModel{
    private String name;
    private String phoneNo;
    private String hashedPassword;
}
