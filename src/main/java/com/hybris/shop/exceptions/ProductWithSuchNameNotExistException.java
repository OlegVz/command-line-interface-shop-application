package com.hybris.shop.exceptions;

public class ProductWithSuchNameNotExistException extends RuntimeException{
    public ProductWithSuchNameNotExistException(String productName) {
        super("Product with such name not exist: " + productName);
    }
}
