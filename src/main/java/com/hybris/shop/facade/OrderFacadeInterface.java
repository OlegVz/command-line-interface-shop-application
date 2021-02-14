package com.hybris.shop.facade;

import com.hybris.shop.dto.NewOrderDto;
import com.hybris.shop.dto.OrderDto;
import com.hybris.shop.dto.UserOrdersDto;

import java.util.List;

public interface OrderFacadeInterface extends FacadeInterface<OrderDto, NewOrderDto, Long> {
    List<UserOrdersDto> findAllOrdersWitProducts();
}
