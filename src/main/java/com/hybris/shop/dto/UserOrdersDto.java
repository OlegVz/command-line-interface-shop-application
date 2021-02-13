package com.hybris.shop.dto;

import com.hybris.shop.annotations.ColumnNameAlias;
import lombok.Data;

import java.util.List;

@Data
public class UserOrdersDto {
    @ColumnNameAlias(alias = "Order ID")
    private Long id;

    @ColumnNameAlias(alias = "Products total price")
    List<Integer> productTotalPrice;

    @ColumnNameAlias(alias = "Product name")
    List<String> productNames;

    @ColumnNameAlias(alias = "Product quantity")
    List<Integer> quantity;

    private String status;

    @ColumnNameAlias(alias = "Order created date")
    private String createdAt;



}
