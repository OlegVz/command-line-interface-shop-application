package com.hybris.shop.service.impl;

import com.hybris.shop.exceptions.orderExceptions.OrderNotFoundByIdException;
import com.hybris.shop.exceptions.orderItemExceptions.OrderItemNotFoundByIdException;
import com.hybris.shop.exceptions.productExceptions.ProductNotFoundByIdException;
import com.hybris.shop.model.OrderItem;
import com.hybris.shop.model.idClasses.OrderItemId;
import com.hybris.shop.repository.OrderItemRepository;
import com.hybris.shop.repository.OrderRepository;
import com.hybris.shop.repository.ProductRepository;
import com.hybris.shop.service.ServiceInterface;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

//@Service
public class OrderItemService implements ServiceInterface<OrderItem, OrderItemId> {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

//    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository,
                            OrderRepository orderRepository,
                            ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        Long orderId = orderItem.getOrder().getId();
        Long productId = orderItem.getProduct().getId();

        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundByIdException(orderId);
        }

        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundByIdException(productId);
        }

        return orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItem findById(OrderItemId id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundByIdException(id));
    }

    @Override
    public boolean existsById(OrderItemId id) {
        return orderItemRepository.existsById(id);
    }

    @Override
    public void deleteById(OrderItemId id) {
        if (orderItemRepository.existsById(id)) {
            orderItemRepository.deleteById(id);
        } else {
            throw new OrderItemNotFoundByIdException(id);
        }
    }

    @Override
    public List<OrderItem> findAll() {
        return StreamSupport.stream(orderItemRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

}
