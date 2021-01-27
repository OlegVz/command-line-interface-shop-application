package com.hybris.shop.service.impl;

import com.hybris.shop.exceptions.UserNotFoundByIdException;
import com.hybris.shop.model.User;
import com.hybris.shop.repository.UserRepository;
import com.hybris.shop.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundByIdException(id));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
