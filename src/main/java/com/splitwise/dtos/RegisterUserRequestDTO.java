package com.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequestDTO {
    private String name;
    private String password;
    private String phoneNo;
}
