package com.hybris.shop.exceptions.userExceptions;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(String password) {
        super("Invalid password: " + password);
    }
}
