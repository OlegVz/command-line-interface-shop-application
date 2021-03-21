package com.hybris.shop.util.impl;

import com.hybris.shop.util.EmailValidatorUtilInterface;

import java.util.regex.Pattern;
import java.util.stream.Stream;

//@Component
public class EmailValidatorUtil implements EmailValidatorUtilInterface {

    private static final String EMAIL_REGEX =
            "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @Override
    public boolean validateEmail(String email) {
        if (email.length() > 254) {
            return false;
        }
        boolean b = Stream.of(email)
                .map(s -> s.split("@")[0])
                .anyMatch(strings -> strings.length() > 64);
        return !b && Pattern.matches(EMAIL_REGEX, email);
    }

}
