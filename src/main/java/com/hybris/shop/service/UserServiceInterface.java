package com.hybris.shop.service;

import com.hybris.shop.model.User;

public interface UserServiceInterface extends ServiceInterface<User, Long> {
    boolean existsByEmail(String email);
}
