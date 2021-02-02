package com.hybris.shop.exceptions.orderItemExceptions;

import com.hybris.shop.model.idClasses.OrderItemId;

public class OrderItemNotFoundByIdException extends RuntimeException {
    public OrderItemNotFoundByIdException(OrderItemId id) {
        super("Order item with id " + id + " was not found");
    }
}
