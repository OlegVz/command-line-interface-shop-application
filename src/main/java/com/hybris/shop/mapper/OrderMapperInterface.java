package com.hybris.shop.mapper;

import com.hybris.shop.dto.NewOrderDto;
import com.hybris.shop.dto.OrderDto;
import com.hybris.shop.dto.UserOrdersDto;
import com.hybris.shop.model.Order;

import javax.annotation.PostConstruct;

public interface OrderMapperInterface {
    @PostConstruct
    void setupMapper();

    OrderDto toOrderDtoFromEntity(Order order);

    Order toEntityFromNewOrderDto(NewOrderDto newOrderDto);

    UserOrdersDto toUserOrdersDto(Order order);
}
