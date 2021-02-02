package com.hybris.shop.service.impl;

import com.hybris.shop.exceptions.orderExceptions.OrderNotFoundByIdException;
import com.hybris.shop.exceptions.userExceptions.UserNotFoundByIdException;
import com.hybris.shop.model.Order;
import com.hybris.shop.repository.OrderRepository;
import com.hybris.shop.repository.UserRepository;
import com.hybris.shop.service.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements ServiceInterface<Order, Long> {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Order save(Order order) {
        Long userId = order.getUser().getId();
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundByIdException(userId);
        }

        return orderRepository.save(order);
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundByIdException(id));
    }

    @Override
    public boolean existsById(Long id) {
        return orderRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        } else {
            throw new OrderNotFoundByIdException(id);
        }
    }
}
