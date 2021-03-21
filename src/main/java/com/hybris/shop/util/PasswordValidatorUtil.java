package com.hybris.shop.util;

import java.util.regex.Pattern;

//@Component
public class PasswordValidatorUtil {

private static final String PASSWORD_REGEX =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&(){}\\[\\]:;<>,?/~_+-=|]).{8,32}$";

    public boolean validatePassword(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }
}
