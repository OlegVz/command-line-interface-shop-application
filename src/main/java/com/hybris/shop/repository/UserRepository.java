package com.hybris.shop.repository;

import com.hybris.shop.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
