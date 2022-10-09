package com.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddGroupRequestDTO extends AuthenticatedRequestDTO{
    private String name;
}
