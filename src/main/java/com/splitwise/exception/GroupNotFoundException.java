package com.splitwise.exception;

public class GroupNotFoundException extends Exception{
    public GroupNotFoundException(Long groupId){
        super("Group "+ groupId + " Not Found");
    }
}
