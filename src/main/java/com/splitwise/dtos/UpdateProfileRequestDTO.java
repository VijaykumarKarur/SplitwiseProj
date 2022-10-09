package com.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequestDTO extends AuthenticatedRequestDTO{
    private String name;
    private String phoneNo;
    private String password;
}
