package com.hybris.shop.dto;

import com.hybris.shop.annotations.ColumnNameAlias;
import com.hybris.shop.model.Order;
import com.hybris.shop.model.Product;
import lombok.Data;

@Data
public class OrderItemDto {

    @ColumnNameAlias(alias = "Order")
    private OrderDto order;

    @ColumnNameAlias(alias = "Product")
    private ProductDto product;

    @ColumnNameAlias(alias = "Quantity")
    private Integer quantity;
}
