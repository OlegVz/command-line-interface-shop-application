package com.hybris.shop.facade;

import com.hybris.shop.dto.NewUserDto;
import com.hybris.shop.dto.OrderDto;
import com.hybris.shop.dto.UserDto;

import java.util.List;

public interface UserFacadeInterface extends FacadeInterface<UserDto, NewUserDto, Long> {
    boolean existsByEmail(String email);

    List<OrderDto> findAllUserOrders(Long userId);
}
