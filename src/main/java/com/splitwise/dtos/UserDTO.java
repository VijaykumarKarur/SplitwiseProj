package com.splitwise.dtos;

import com.splitwise.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String phoneNo;
    private long id;

    public static UserDTO from(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.name = user.getName();
        userDTO.phoneNo = user.getPhoneNo();
        userDTO.id = user.getId();
        return userDTO;
    }
}
