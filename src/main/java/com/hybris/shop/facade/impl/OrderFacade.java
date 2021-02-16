package com.hybris.shop.facade.impl;

import com.hybris.shop.dto.NewOrderDto;
import com.hybris.shop.dto.OrderDto;
import com.hybris.shop.dto.UserOrdersDto;
import com.hybris.shop.facade.OrderFacadeInterface;
import com.hybris.shop.mapper.OrderMapper;
import com.hybris.shop.model.Order;
import com.hybris.shop.model.User;
import com.hybris.shop.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderFacade implements OrderFacadeInterface {

    private final OrderService orderService;

    private final OrderMapper orderMapper;

    @Autowired
    public OrderFacade(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderDto save(NewOrderDto newOrderDto) {
        newOrderDto.setStatus(Order.OrderStatus.NEW_ORDER.getStatus());
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        newOrderDto.setCreatedAt(format);

        Order savedOrderItem = orderService.save(orderMapper.toEntityFromNewOrderDto(newOrderDto));

        return orderMapper.toOrderDtoFromEntity(savedOrderItem);
    }

    @Override
    public OrderDto findById(Long id) {
        Order orderById = orderService.findById(id);

        return orderMapper.toOrderDtoFromEntity(orderById);
    }

    @Override
    public OrderDto update(Long id, NewOrderDto newOrderDto) {
        Order newDataObject = orderMapper.toEntityFromNewOrderDto(newOrderDto);
        User user = orderService.findById(id).getUser();
        newDataObject.setUser(user);

        return orderMapper.toOrderDtoFromEntity(orderService.update(id, newDataObject));
    }

    @Override
    public boolean existsById(Long id) {
        return orderService.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        orderService.deleteById(id);
    }

    @Override
    public List<OrderDto> findAll() {
        return orderService.findAll().stream()
                .map(orderMapper::toOrderDtoFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserOrdersDto> findAllOrdersWitProducts() {
        List<Order> orders = orderService.findAll();

        return orders.stream()
                .distinct()
                .map(orderMapper::toUserOrdersDto)
                .collect(Collectors.toList());
    }
}
