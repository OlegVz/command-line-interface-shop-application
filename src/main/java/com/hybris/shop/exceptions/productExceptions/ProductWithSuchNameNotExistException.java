package com.hybris.shop.exceptions.productExceptions;

public class ProductWithSuchNameNotExistException extends RuntimeException{
    public ProductWithSuchNameNotExistException(String productName) {
        super("Product with such name not exist: " + productName);
    }
}
