package com.hybris.shop.exceptions;

public class ProductNotFoundByIdException extends RuntimeException{
    public ProductNotFoundByIdException(Long id) {
        super("Product with id " + id + " was not found");
    }
}
