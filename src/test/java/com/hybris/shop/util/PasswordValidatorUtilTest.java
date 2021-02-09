package com.hybris.shop.util;

import com.hybris.shop.ShopApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ShopApplication.class})
class PasswordValidatorUtilTest {

    private static final String VALID_PASSWORD = "f2fdeDw#5gB";

    private static final String[] INVALID_PASSWORDS = {
            "f2Dw5gB",
            "fdejDweRgB",
            "f2fdetw5gdt"
    };

    @Autowired
    private PasswordValidatorUtil passwordValidatorUtil;

    @Test
    void shouldValidatePassword() {
        //given
        //when
        boolean b = passwordValidatorUtil.validatePassword(VALID_PASSWORD);

        //then
        assertTrue(b);
    }

    @Test
    void shouldNotValidatePassword() {
        //given
        //when
        //then
        Arrays.stream(INVALID_PASSWORDS)
                .forEach(password -> assertFalse(passwordValidatorUtil.validatePassword(password)));
    }
}