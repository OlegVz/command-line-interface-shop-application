package com.hybris.shop.service.impl;

import com.hybris.shop.exceptions.userExceptions.UserNotFoundByIdException;
import com.hybris.shop.exceptions.userExceptions.UserWithSuchEmailExistException;
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
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) throws UserWithSuchEmailExistException {
        String email = user.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new UserWithSuchEmailExistException(email);
        }

        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) throws UserNotFoundByIdException{
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundByIdException(id));
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) throws UserNotFoundByIdException{
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundByIdException(id);
        }
    }
}
