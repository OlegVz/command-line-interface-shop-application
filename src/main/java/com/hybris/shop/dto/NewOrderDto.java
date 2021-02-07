package com.hybris.shop.dto;

import com.hybris.shop.model.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class NewOrderDto {
    private Long userId;

    private String status;

    private String createdAt;

    List<OrderItem> orderItems;
}
