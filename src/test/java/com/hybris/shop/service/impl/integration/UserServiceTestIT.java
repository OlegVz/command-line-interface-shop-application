package com.hybris.shop.service.impl.integration;

import com.hybris.shop.ShopApplication;
import com.hybris.shop.model.User;
import com.hybris.shop.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {ShopApplication.class})
public class UserServiceTestIT {

    private static final String TEST_EMAIL = "testEmail@email.com";
    private static final String PASSWORD = "1234@qwerty";

    private User user;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void init() {
        user = new User();
        user.setEmail(TEST_EMAIL);
        user.setPassword(PASSWORD);

    }

    @Test
    void shouldSaveNewUser() {
        //given
        //when
        User savedUser = userService.save(user);

        //then
        assertNotNull(savedUser.getId());
        assertEquals(TEST_EMAIL, savedUser.getEmail());
        assertEquals(PASSWORD, savedUser.getPassword());
    }
}
