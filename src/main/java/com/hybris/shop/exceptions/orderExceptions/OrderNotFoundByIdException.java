package com.hybris.shop.exceptions.orderExceptions;

public class OrderNotFoundByIdException extends RuntimeException{
    public OrderNotFoundByIdException(Long id) {
        super("Order with id " + id + " was not found");
    }
}
