package com.splitwise.exception;

public class InvalidCommandException extends Exception{
    public InvalidCommandException(String command){
        super("Invalid Command :" + command);
    }
}
