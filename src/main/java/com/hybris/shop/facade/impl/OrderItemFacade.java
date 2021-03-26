package com.hybris.shop.facade.impl;

import com.hybris.shop.dto.NewOrderItemDto;
import com.hybris.shop.dto.OrderItemDto;
import com.hybris.shop.dto.orderItemDtosId.OrderItemDtoId;
import com.hybris.shop.exceptions.orderItemExceptions.OrderItemNotFoundByIdException;
import com.hybris.shop.facade.OrderItemFacadeInterface;
import com.hybris.shop.mapper.impl.OrderItemMapper;
import com.hybris.shop.model.OrderItem;
import com.hybris.shop.model.idClasses.OrderItemId;
import com.hybris.shop.service.ServiceInterface;

import java.util.List;
import java.util.stream.Collectors;

//@Component
public class OrderItemFacade implements OrderItemFacadeInterface {

    private final ServiceInterface<OrderItem, OrderItemId> orderItemService;

    private final OrderItemMapper orderItemMapper;

//    @Autowired
    public OrderItemFacade(ServiceInterface<OrderItem, OrderItemId> orderItemService, OrderItemMapper orderItemMapper) {
        this.orderItemService = orderItemService;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    public OrderItemDto save(NewOrderItemDto newOrderItemDto) {
        OrderItem savedOrderItem = orderItemService.save(orderItemMapper.toEntityFromNewOrderItemDto(newOrderItemDto));

        return orderItemMapper.toOrderItemDtoFromEntity(savedOrderItem);
    }

    @Override
    public OrderItemDto findById(OrderItemDtoId id) {
        OrderItemId orderItemId = orderItemMapper.fromOrderItemDtoIdToOrderItemId(id);

        OrderItem orderItemById = orderItemService.findById(orderItemId);

        return orderItemMapper.toOrderItemDtoFromEntity(orderItemById);
    }

    @Override
    public OrderItemDto update(OrderItemDtoId id, NewOrderItemDto newOrderItemDto) {
        OrderItemId orderItemId = orderItemMapper.fromOrderItemDtoIdToOrderItemId(id);

        if (!orderItemService.existsById(orderItemId)) {
            throw new OrderItemNotFoundByIdException(orderItemId);
        }

        OrderItem updatedOrderItem =
                orderItemService.update(orderItemId, orderItemMapper.toEntityFromNewOrderItemDto(newOrderItemDto));

        return orderItemMapper.toOrderItemDtoFromEntity(updatedOrderItem);
    }

    @Override
    public boolean existsById(OrderItemDtoId id) {
        OrderItemId orderItemId = orderItemMapper.fromOrderItemDtoIdToOrderItemId(id);

        return orderItemService.existsById(orderItemId);
    }

    @Override
    public void deleteById(OrderItemDtoId id) {
        OrderItemId orderItemId = orderItemMapper.fromOrderItemDtoIdToOrderItemId(id);

        orderItemService.deleteById(orderItemId);
    }

    @Override
    public List<OrderItemDto> findAll() {
        return orderItemService.findAll().stream()
                .map(orderItemMapper::toOrderItemDtoFromEntity)
                .collect(Collectors.toList());
    }
}
