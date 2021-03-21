package com.hybris.shop.mapper;

import com.hybris.shop.dto.NewUserDto;
import com.hybris.shop.dto.UserDto;
import com.hybris.shop.model.User;

public interface UserMapperInterface {
    User toEntityFromNewUserDto(NewUserDto newUserDto);

    UserDto toUserDtoFromUser(User user);
}
