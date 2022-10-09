package com.splitwise.services.passwordencoder;

import org.springframework.stereotype.Service;

@Service
public class PasswordEncoder implements IPasswordEncoder {
    @Override
    public String encodePassword(String password) {
        return "Encoded"+password+"Password";
    }
}
