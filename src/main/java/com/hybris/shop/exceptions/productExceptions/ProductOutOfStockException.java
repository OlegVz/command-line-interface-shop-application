package com.hybris.shop.exceptions.productExceptions;

public class ProductOutOfStockException extends RuntimeException {
    public ProductOutOfStockException(String productName) {
        super(String.format("Product %s out of stock!", productName));
    }
}
