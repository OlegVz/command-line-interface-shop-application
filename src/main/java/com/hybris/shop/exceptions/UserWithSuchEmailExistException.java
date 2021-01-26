package com.hybris.shop.exceptions;

public class UserWithSuchEmailExistException extends RuntimeException {
    public UserWithSuchEmailExistException(String email) {
        super("User with such email exist: " + email);
    }
}
