package com.hybris.shop.facade;

import com.hybris.shop.dto.NewUserDto;
import com.hybris.shop.dto.UserDto;

public interface UserFacadeInterface extends FacadeInterface<UserDto, NewUserDto, Long> {
    boolean existByEmail(String email);
}
