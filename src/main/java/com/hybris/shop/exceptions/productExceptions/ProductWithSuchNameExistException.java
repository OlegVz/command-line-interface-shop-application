package com.hybris.shop.exceptions.productExceptions;

public class ProductWithSuchNameExistException extends RuntimeException{
    public ProductWithSuchNameExistException(String productName) {
        super("Product with such name exist: " + productName);
    }
}
