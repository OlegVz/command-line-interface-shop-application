package com.hybris.shop.service;

import com.hybris.shop.model.User;

public interface UserService {
    User save(User user);

    User findById(Long id);

    User update(Long id, User user);

    void deleteById(Long id);

    boolean existByEmail(String email);
}
