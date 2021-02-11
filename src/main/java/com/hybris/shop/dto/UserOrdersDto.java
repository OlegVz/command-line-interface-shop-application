package com.hybris.shop.dto;

import com.hybris.shop.annotations.ColumnNameAlias;
import lombok.Data;

import java.util.List;

@Data
public class UserOrdersDto {
    @ColumnNameAlias(alias = "Order ID")
    private Long id;

    @ColumnNameAlias(alias = "Status")
    private String status;

    @ColumnNameAlias(alias = "Date")
    private String createdAt;

    @ColumnNameAlias(alias = "Products")
    List<String> productNames;

    @ColumnNameAlias(alias = "Quantity")
    List<Integer> quantity;
}
