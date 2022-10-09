package com.splitwise.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberRequestDTO extends AuthenticatedRequestDTO{
    private Long groupId;
    private Long memberId;
}
