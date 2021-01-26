package com.hybris.shop.model.idClasses;

import com.hybris.shop.model.Order;
import com.hybris.shop.model.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class OrderItemId implements Serializable {

    private Order order;
    private Product product;
}
