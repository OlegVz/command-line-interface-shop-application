package com.hybris.shop.dto;

import com.hybris.shop.annotations.ColumnNameAlias;
import com.hybris.shop.model.Product;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
public class ProductDto {

    @ColumnNameAlias(alias = "Product ID")
    private Long id;


    @ColumnNameAlias(alias = "Product name")
    private String name;

    @ColumnNameAlias(alias = "Price for piece")
    private Integer price;

    @ColumnNameAlias(alias = "Product status")
    private Product.ProductStatus status;

    @ColumnNameAlias(alias = "Date")
    private LocalDateTime createdAt;
}
