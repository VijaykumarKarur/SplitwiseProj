package com.splitwise.dtos;

import com.splitwise.models.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddGroupExpenseResponseDTO extends ResponseDTO{
    private Group group;
}
