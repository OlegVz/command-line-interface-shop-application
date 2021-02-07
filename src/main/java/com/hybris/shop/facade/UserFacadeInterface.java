package com.hybris.shop.facade;

import com.hybris.shop.dto.NewUserDto;
import com.hybris.shop.dto.OrderDto;
import com.hybris.shop.dto.UserDto;
import com.hybris.shop.model.Order;

import java.util.List;

public interface UserFacadeInterface extends FacadeInterface<UserDto, NewUserDto, Long> {
    boolean existByEmail(String email);

    List<OrderDto> findAllUserOrders(Long userId);
}
