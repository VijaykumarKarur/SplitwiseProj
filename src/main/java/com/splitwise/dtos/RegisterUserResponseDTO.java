package com.splitwise.dtos;
import com.splitwise.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserResponseDTO extends ResponseDTO{
    private User user;
}
