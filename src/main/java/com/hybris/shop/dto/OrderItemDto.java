package com.hybris.shop.dto;

import com.hybris.shop.model.Order;
import com.hybris.shop.model.Product;
import lombok.Data;

@Data
public class OrderItemDto {

    private Order order;

    private Product product;

    private Integer quantity;
}
