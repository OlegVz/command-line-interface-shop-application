package com.hybris.shop.facade.impl;

import com.hybris.shop.dto.NewOrderDto;
import com.hybris.shop.dto.OrderDto;
import com.hybris.shop.facade.OrderFacadeInterface;
import com.hybris.shop.mapper.OrderMapper;
import com.hybris.shop.model.Order;
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
        newOrderDto.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        Order order = orderMapper.toEntityFromNewOrderDto(newOrderDto);

        return orderMapper.toOrderDtoFromEntity(orderService.save(order));
    }

    @Override
    public OrderDto findById(Long id) {
        return orderMapper.toOrderDtoFromEntity(orderService.findById(id));
    }

    @Override
    public OrderDto update(Long id, NewOrderDto newOrderDto) {
        Order newDataObject = orderMapper.toEntityFromNewOrderDto(newOrderDto);

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
}
