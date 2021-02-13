package com.hybris.shop.dto;

import lombok.Data;

@Data
public class NewOrderDto {

    private Long userId;

    private String status;

    private String createdAt;
}
