package com.hybris.shop.dto;

import lombok.Data;

@Data
public class NewOrderItemDto {

    private Long orderId;

    private Long productId;

    private Integer quantity;
}
