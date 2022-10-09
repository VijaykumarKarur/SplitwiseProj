package com.splitwise.exception;

public class InvalidGroupAdminException extends Exception{
    public InvalidGroupAdminException(Long userId, Long groupId){
        super("User " + userId + " is not the admin of Group " + groupId);
    }
}
