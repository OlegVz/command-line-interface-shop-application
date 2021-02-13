package com.hybris.shop.facade;

import com.hybris.shop.dto.NewUserDto;
import com.hybris.shop.dto.UserDto;
import com.hybris.shop.dto.UserOrdersDto;

import java.util.List;

public interface UserFacadeInterface extends FacadeInterface<UserDto, NewUserDto, Long> {
    boolean existsByEmail(String email);

    List<UserOrdersDto> findAllUserOrders(Long userId);

    Long logIn(NewUserDto newUserDto);

    boolean chekPassword(Long currentUserId, String password);
}
