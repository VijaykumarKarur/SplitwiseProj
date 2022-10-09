package com.splitwise.dtos;

import com.splitwise.models.Group;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserGroupsResponseDTO extends ResponseDTO{
    private List<Group> groups;
}
