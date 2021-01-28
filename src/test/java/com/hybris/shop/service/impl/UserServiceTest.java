package com.hybris.shop.service.impl;

import com.hybris.shop.exceptions.UserNotFoundByIdException;
import com.hybris.shop.exceptions.UserWithSuchEmailExistException;
import com.hybris.shop.model.User;
import com.hybris.shop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String TEST_EMAIL = "testEmail@email.com";
    private static final String PASSWORD = "1234@qwerty";
    private static final long NOT_EXIST_ID = 0L;
    private static final long USER_ID = 1L;
    private static final String NEW_EMAIL = "updEmail@email.com";
    private static final String NEW_PASSWORD = "qwerty";

    private User userInDB;
    private User newUser;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        userInDB = new User();
        userInDB.setId(USER_ID);
        userInDB.setEmail(TEST_EMAIL);
        userInDB.setPassword(PASSWORD);

        newUser = new User();
        newUser.setEmail(NEW_EMAIL);
        newUser.setPassword(NEW_PASSWORD);
    }

    @Test
    void shouldThrowExceptionWhenTrySaveUserWithExistingEmail() {
        //given
        //when
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        //then
        UserWithSuchEmailExistException exception =
                assertThrows(UserWithSuchEmailExistException.class, () -> userService.save(newUser));

        assertEquals(String.format("User with such email exist: %s", NEW_EMAIL), exception.getMessage());
    }

    @Test
    void shouldReturnTrueWhenExistByEmail() {
        //given
        //when
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        boolean existByEmail = userService.existByEmail(TEST_EMAIL);

        //then
        assertTrue(existByEmail);
    }

    @Test
    void shouldReturnFalseWhenExistByEmail() {
        //given
        //when
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        boolean existByEmail = userService.existByEmail(TEST_EMAIL);

        //then
        assertFalse(existByEmail);
    }

    @Test
    void shouldFindAndReturnUserById() {
        //given
        //when
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userInDB));

        User byId = userService.findById(USER_ID);

        //then
        assertEquals(USER_ID, byId.getId());
    }

    @Test
    void shouldThrowExceptionWhenUserWithSuchIdNotExist() {
        //given
        //when
        when(userRepository.findById(anyLong())).thenThrow(new UserNotFoundByIdException(NOT_EXIST_ID));

        //then
        UserNotFoundByIdException exception = assertThrows(UserNotFoundByIdException.class,
                () -> userService.findById(NOT_EXIST_ID));

        assertEquals(String.format("User with id %s was not found", NOT_EXIST_ID), exception.getMessage());
    }

    @Test
    void shouldUpdateUserData() {
        //given
        //when
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userInDB));
        when(userService.save(userInDB)).thenReturn(userInDB);

        User updatedUser = userService.update(USER_ID, newUser);

        //then
        assertEquals(USER_ID, updatedUser.getId());
        assertEquals(NEW_EMAIL, updatedUser.getEmail());
        assertEquals(NEW_PASSWORD, updatedUser.getPassword());
    }

    @Test
    void shouldUpdateOnlyUserEmail() {
        //given
        newUser.setPassword(null);

        //when
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userInDB));
        when(userService.save(userInDB)).thenReturn(userInDB);

        User updatedUser = userService.update(USER_ID, newUser);

        //then
        assertEquals(USER_ID, updatedUser.getId());
        assertEquals(NEW_EMAIL, updatedUser.getEmail());
        assertEquals(userInDB.getPassword(), updatedUser.getPassword());
    }

    @Test
    void shouldUpdateOnlyUserPassword() {
        //given
        newUser.setEmail(null);

        //when
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userInDB));
        when(userService.save(userInDB)).thenReturn(userInDB);

        User updatedUser = userService.update(USER_ID, newUser);

        //then
        assertEquals(USER_ID, updatedUser.getId());
        assertEquals(userInDB.getEmail(), updatedUser.getEmail());
        assertEquals(NEW_PASSWORD, updatedUser.getPassword());
    }


    @Test
    void shouldThrowExceptionWhenTryUpdateUserWithNotExistId() {
        //given
        //when
        when(userRepository.findById(anyLong())).thenThrow(new UserNotFoundByIdException(NOT_EXIST_ID));

        //then
        UserNotFoundByIdException exception = assertThrows(UserNotFoundByIdException.class,
                () -> userService.update(NOT_EXIST_ID, newUser));

        assertEquals(String.format("User with id %s was not found", NOT_EXIST_ID), exception.getMessage());
    }

    @Test
    void shouldDeleteUserById() {
        //given
        //when
        when(userRepository.existsById(anyLong())).thenReturn(true);
        userService.deleteById(USER_ID);

        //then
        verify(userRepository, times(1)).deleteById(USER_ID);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowExceptionWhenDeleteUserByIdWithNotExistingId() {
        //given
        //when
        when(userRepository.existsById(anyLong())).thenReturn(false);

        //then
        UserNotFoundByIdException exception =
                assertThrows(UserNotFoundByIdException.class, () -> userService.deleteById(NOT_EXIST_ID));
        assertEquals(String.format("User with id %s was not found", NOT_EXIST_ID), exception.getMessage());
    }
}