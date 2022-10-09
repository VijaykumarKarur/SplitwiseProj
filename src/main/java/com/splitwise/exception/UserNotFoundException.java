package com.splitwise.exception;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(Long userId){
        super("User "+ userId + " Not Found");
    }
}
