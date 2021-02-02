package com.hybris.shop.exceptions.userExceptions;

public class UserWithSuchEmailExistException extends RuntimeException {
    public UserWithSuchEmailExistException(String email) {
        super("User with such email exist: " + email);
    }
}
