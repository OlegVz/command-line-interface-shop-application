package com.hybris.shop.exceptions;

public class UserNotFoundByIdException extends RuntimeException{
    public UserNotFoundByIdException(Long id) {
        super("User with id " + id + " was not found");
    }
}
