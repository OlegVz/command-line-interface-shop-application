package com.hybris.shop.facade.impl;

import com.hybris.shop.dto.NewUserDto;
import com.hybris.shop.dto.UserDto;
import com.hybris.shop.exceptions.userExceptions.UserNotFoundByIdException;
import com.hybris.shop.mapper.OrderMapper;
import com.hybris.shop.mapper.UserMapper;
import com.hybris.shop.model.User;
import com.hybris.shop.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {

    private static final String VALID_USER_EMAIL = "userEmail@email.com";

    private static final String VALID_PASSWORD = "f2fdeDw#5gB";
    private static final long USER_ID = 1L;
    private static final long NOT_EXISTING_USER_ID = 0L;

    private NewUserDto newUserDto;
    private UserDto userDto;
    private User user;
    private User newUser;


    @InjectMocks
    private UserFacade userFacade;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private OrderMapper orderMapper;

    @BeforeEach
    void init() {
        newUserDto = new NewUserDto();
        newUserDto.setEmail(VALID_USER_EMAIL);
        newUserDto.setPassword(VALID_PASSWORD);

        userDto = new UserDto();
        userDto.setId(USER_ID);
        userDto.setEmail(VALID_USER_EMAIL);

        user = new User();
        user.setId(USER_ID);
        user.setEmail(VALID_USER_EMAIL);
        user.setPassword(VALID_PASSWORD);

        newUser = new User();
        newUser.setEmail(VALID_USER_EMAIL);
        newUser.setPassword(VALID_PASSWORD);
    }

    @Test
    void shouldReturnTrueWhenExistByEmail() {
        //given
        //when
        when(userService.existsByEmail(anyString())).thenReturn(true);

        boolean b = userFacade.existsByEmail(VALID_USER_EMAIL);

        //then
        assertTrue(b);
    }

    @Test
    void shouldReturnFalseWhenExistByEmail() {
        //given
        //when
        when(userService.existsByEmail(anyString())).thenReturn(false);

        boolean b = userFacade.existsByEmail(VALID_USER_EMAIL);

        //then
        assertFalse(b);
    }

    @Test
    void shouldSaveAndReturnNewUser() {
        //given
        //when
        when(userMapper.toEntityFromNewUserDto(newUserDto)).thenReturn(newUser);
        when(userService.save(newUser)).thenReturn(user);
        when(userMapper.toUserDtoFromUser(user)).thenReturn(userDto);

        UserDto savedUser = userFacade.save(newUserDto);

        //then
        assertEquals(USER_ID, savedUser.getId());
        assertEquals(VALID_USER_EMAIL, savedUser.getEmail());
    }

    @Test
    void shouldFindUserByIdAndReturnUserDto() {
        //given
        //when
        when(userService.findById(anyLong())).thenReturn(user);
        when(userMapper.toUserDtoFromUser(user)).thenReturn(userDto);

        UserDto userById = userFacade.findById(USER_ID);

        //then
        assertEquals(USER_ID, userById.getId());
    }

    @Test
    void shouldThrowExceptionWhenTryFindUserByNotExistingId() {
        //given
        //when
        when(userService.findById(anyLong())).thenThrow(new UserNotFoundByIdException(NOT_EXISTING_USER_ID));

        //then
        UserNotFoundByIdException exception = assertThrows(UserNotFoundByIdException.class,
                () -> userFacade.findById(NOT_EXISTING_USER_ID));

        assertEquals(String.format("User with id %s was not found", NOT_EXISTING_USER_ID), exception.getMessage());
    }

    @Test
    void shouldReturnTrueWhenExistById() {
        //given
        //when
        when(userService.existsById(anyLong())).thenReturn(true);

        boolean b = userFacade.existsById(USER_ID);

        //then
        assertTrue(b);
    }

    @Test
    void shouldReturnFalseWhenExistById() {
        //given
        //when
        when(userService.existsById(anyLong())).thenReturn(false);

        boolean b = userFacade.existsById(NOT_EXISTING_USER_ID);

        //then
        assertFalse(b);
    }

    @Test
    void shouldDeleteUserById() {
        //given
        //when
        userFacade.deleteById(USER_ID);

        //then
        verify(userService, times(1)).deleteById(USER_ID);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void findAllUserOrders() {
    }
}