package com.hybris.shop.dto;

import com.hybris.shop.model.Product;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductDto {

    private Long id;

    private String name;

    private Integer price;

    private Product.ProductStatus status;

    private LocalDateTime createdAt;
}
