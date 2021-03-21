package com.hybris.shop.mapper;

import com.hybris.shop.dto.NewOrderItemDto;
import com.hybris.shop.dto.OrderItemDto;
import com.hybris.shop.dto.orderItemDtosId.OrderItemDtoId;
import com.hybris.shop.model.OrderItem;
import com.hybris.shop.model.idClasses.OrderItemId;

public interface OrderItemMapperInterface {
    OrderItem toEntityFromNewOrderItemDto(NewOrderItemDto newOrderItemDto);

    OrderItemDto toOrderItemDtoFromEntity(OrderItem orderItem);

    OrderItemId fromOrderItemDtoIdToOrderItemId(OrderItemDtoId orderItemDtoId);
}
