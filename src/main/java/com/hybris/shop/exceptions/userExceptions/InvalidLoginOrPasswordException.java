package com.hybris.shop.exceptions.userExceptions;

public class InvalidLoginOrPasswordException extends RuntimeException{
    public InvalidLoginOrPasswordException() {
        super("Invalid login or password");
    }
}
