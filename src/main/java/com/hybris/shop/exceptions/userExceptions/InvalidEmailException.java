package com.hybris.shop.exceptions.userExceptions;

public class InvalidEmailException extends RuntimeException{
    public InvalidEmailException(String email) {
        super("Invalid email: " + email);
    }
}
