package com.splitwise.exception;

public class InvalidParameterException extends Exception{
    public InvalidParameterException(String parameter){
        super("Invalid Parameter - "+ parameter);
    }
}
