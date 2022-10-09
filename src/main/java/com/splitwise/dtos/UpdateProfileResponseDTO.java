package com.splitwise.dtos;

import com.splitwise.models.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateProfileResponseDTO extends ResponseDTO{
    private User user;
}
