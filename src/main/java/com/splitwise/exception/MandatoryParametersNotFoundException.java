package com.splitwise.exception;

import java.util.List;

public class MandatoryParametersNotFoundException extends Exception{
    public MandatoryParametersNotFoundException(List<String> mandatoryList){
        super("Mandatory Parameters - " + mandatoryList);
    }
}
