package com.hybris.shop.service.impl;

import com.hybris.shop.exceptions.UserNotFoundByIdException;
import com.hybris.shop.exceptions.UserWithSuchEmailExistException;
import com.hybris.shop.model.User;
import com.hybris.shop.repository.UserRepository;
import com.hybris.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;

@Service(value = "UserService")
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        String email = user.getEmail();

        if (existByEmail(email)) {
            throw new UserWithSuchEmailExistException(email);
        }

        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundByIdException(id));
    }

    @Override
    public User update(Long id, User newDataUser) {

        newDataUser.setId(id);

        User oldDataUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundByIdException(id));

        Class<? extends User> userClass = newDataUser.getClass();

        Arrays.stream(userClass.getDeclaredFields())
                .filter(field -> {
                    try {
                        field.setAccessible(true);

                        return field.get(newDataUser) != null;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .forEach(field -> {
                    Field userField = ReflectionUtils.findField(User.class, field.getName());

                    if (userField != null) {
                        userField.setAccessible(true);

                        try {
                            ReflectionUtils.setField(userField, oldDataUser, field.get(newDataUser));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        return userRepository.save(oldDataUser);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
