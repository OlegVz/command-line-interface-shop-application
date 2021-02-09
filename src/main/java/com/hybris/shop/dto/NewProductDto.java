package com.hybris.shop.dto;

import com.hybris.shop.model.Product;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewProductDto {

    private String name;

    private Integer price;

    private Product.ProductStatus status;

    private LocalDateTime createdAt;
}
