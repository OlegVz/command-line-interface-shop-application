package com.hybris.shop.util;

import com.hybris.shop.ShopApplication;
import com.hybris.shop.util.impl.EmailValidatorUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ShopApplication.class})
class EmailValidatorUtilTest {

    private static final String[] VALID_EMAILS = {
            "simple@example.com",
            "very.common@example.com",
            "disposable.style.email.with+symbol@example.com",
            "other.email-with-hyphen@example.com",
            "fully-qualified-domain@example.com",
            "user.name+tag+sorting@example.com",
            "x@example.com",
            "example-indeed@strange-example.com",
            "example@s.example",
            "mailhost!username@example.org",
            "user%example.com@example.org"
    };

    public static final String[] INVALID_EMAILS = {
            "Abc.example.com",
            "A@b@c@example.com",
            "a\"b(c)d,e:f;g<h>i[j\\k]l@example.com",
            "just\"not\"right@example.com",
            "this is\"not\\allowed@example.com",
            "this\\ still\\\"not\\\\allowed@example.com",
            "1234567890123456789012345678901234567890123456789012345678901234+x@example.com",
            "i_like_underscore@but_its_not_allow_in _this_part.example.com"
    };

    @Autowired
    private EmailValidatorUtil emailValidatorUtil;

    @Test
    public void shouldValidateEmail() {
        //given
        //when
        //then
        assertTrue(emailValidatorUtil.validateEmail(VALID_EMAILS[0]));
        assertTrue(emailValidatorUtil.validateEmail(VALID_EMAILS[1]));
        assertTrue(emailValidatorUtil.validateEmail(VALID_EMAILS[2]));
        assertTrue(emailValidatorUtil.validateEmail(VALID_EMAILS[3]));
        assertTrue(emailValidatorUtil.validateEmail(VALID_EMAILS[4]));
        assertTrue(emailValidatorUtil.validateEmail(VALID_EMAILS[5]));
        assertTrue(emailValidatorUtil.validateEmail(VALID_EMAILS[6]));
        assertTrue(emailValidatorUtil.validateEmail(VALID_EMAILS[7]));
        assertTrue(emailValidatorUtil.validateEmail(VALID_EMAILS[8]));
        assertTrue(emailValidatorUtil.validateEmail(VALID_EMAILS[9]));
        assertTrue(emailValidatorUtil.validateEmail(VALID_EMAILS[10]));
    }

    @Test
    public void shouldNotValidateEmail() {
        //given
        //when
        //then
        assertFalse(emailValidatorUtil.validateEmail(INVALID_EMAILS[0]));
        assertFalse(emailValidatorUtil.validateEmail(INVALID_EMAILS[2]));
        assertFalse(emailValidatorUtil.validateEmail(INVALID_EMAILS[3]));
        assertFalse(emailValidatorUtil.validateEmail(INVALID_EMAILS[4]));
        assertFalse(emailValidatorUtil.validateEmail(INVALID_EMAILS[5]));
        assertFalse(emailValidatorUtil.validateEmail(INVALID_EMAILS[6]));
        assertFalse(emailValidatorUtil.validateEmail(INVALID_EMAILS[7]));
    }
}