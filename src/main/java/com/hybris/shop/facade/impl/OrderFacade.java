package com.hybris.shop.facade.impl;

import com.hybris.shop.dto.NewOrderDto;
import com.hybris.shop.dto.OrderDto;
import com.hybris.shop.dto.UserOrdersDto;
import com.hybris.shop.facade.OrderFacadeInterface;
import com.hybris.shop.mapper.OrderMapperInterface;
import com.hybris.shop.model.Order;
import com.hybris.shop.model.User;
import com.hybris.shop.service.ServiceInterface;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

//@Component
public class OrderFacade implements OrderFacadeInterface {

    private final ServiceInterface<Order, Long> orderService;

    private final OrderMapperInterface orderMapperInterface;

//    @Autowired
    public OrderFacade(ServiceInterface<Order, Long> orderService, OrderMapperInterface orderMapperInterface) {
        this.orderService = orderService;
        this.orderMapperInterface = orderMapperInterface;
    }

    @Override
    public OrderDto save(NewOrderDto newOrderDto) {
        newOrderDto.setStatus(Order.OrderStatus.NEW_ORDER.getStatus());
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        newOrderDto.setCreatedAt(format);

        Order savedOrderItem = orderService.save(orderMapperInterface.toEntityFromNewOrderDto(newOrderDto));

        return orderMapperInterface.toOrderDtoFromEntity(savedOrderItem);
    }

    @Override
    public OrderDto findById(Long id) {
        Order orderById = orderService.findById(id);

        return orderMapperInterface.toOrderDtoFromEntity(orderById);
    }

    @Override
    public OrderDto update(Long id, NewOrderDto newOrderDto) {
        Order newDataObject = orderMapperInterface.toEntityFromNewOrderDto(newOrderDto);
        User user = orderService.findById(id).getUser();
        newDataObject.setUser(user);

        return orderMapperInterface.toOrderDtoFromEntity(orderService.update(id, newDataObject));
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
                .map(orderMapperInterface::toOrderDtoFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserOrdersDto> findAllOrdersWitProducts() {
        List<Order> orders = orderService.findAll();

        return orders.stream()
                .distinct()
                .map(orderMapperInterface::toUserOrdersDto)
                .collect(Collectors.toList());
    }
}
