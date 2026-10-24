package com.example.informationsystemslab1.exception.custom;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String username) {
        super("Username '" + username + "' is already taken");
    }
}
