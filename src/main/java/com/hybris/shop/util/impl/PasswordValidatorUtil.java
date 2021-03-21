package com.hybris.shop.util.impl;

import com.hybris.shop.util.PasswordValidatorUtilInterface;

import java.util.regex.Pattern;

//@Component
public class PasswordValidatorUtil implements PasswordValidatorUtilInterface {

private static final String PASSWORD_REGEX =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&(){}\\[\\]:;<>,?/~_+-=|]).{8,32}$";

    @Override
    public boolean validatePassword(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }
}
