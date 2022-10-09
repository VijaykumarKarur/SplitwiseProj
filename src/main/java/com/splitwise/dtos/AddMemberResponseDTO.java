package com.splitwise.dtos;

import com.splitwise.models.Group;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberResponseDTO extends ResponseDTO{
    private Group group;
}
