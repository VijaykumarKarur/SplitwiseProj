package com.splitwise.dtos;

import com.splitwise.models.Group;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddGroupResponseDTO extends ResponseDTO{
    private Group group;
}
