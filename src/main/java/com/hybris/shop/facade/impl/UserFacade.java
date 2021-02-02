package com.hybris.shop.facade.impl;

import com.hybris.shop.dto.NewUserDto;
import com.hybris.shop.dto.UserDto;
import com.hybris.shop.facade.UserFacadeInterface;
import com.hybris.shop.mapper.UserMapper;
import com.hybris.shop.model.User;
import com.hybris.shop.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFacade implements UserFacadeInterface {

    private UserService userService;

    private UserMapper userMapper;

    @Autowired
    public UserFacade(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @Override
    public UserDto save(NewUserDto newUserDto) {
        User savedUser = userService.save(userMapper.toEntityFromNewUserDto(newUserDto));

        return userMapper.toUserDtoFromUser(savedUser);
    }

    @Override
    public UserDto findById(Long id) {
        User byId = userService.findById(id);

        return userMapper.toUserDtoFromUser(byId);
    }

    @Override
    public UserDto update(Long id, NewUserDto newUserDto) {
        User updatedUser = userService.update(id, userMapper.toEntityFromNewUserDto(newUserDto));

        return userMapper.toUserDtoFromUser(updatedUser);
    }

    @Override
    public void deleteById(Long id) {
        userService.deleteById(id);
    }

    @Override
    public boolean existByEmail(String email) {
        return userService.existByEmail(email);
    }
}
