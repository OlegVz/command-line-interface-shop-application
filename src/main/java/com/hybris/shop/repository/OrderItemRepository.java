package com.hybris.shop.repository;

import com.hybris.shop.model.OrderItem;
import com.hybris.shop.model.idClasses.OrderItemId;
import org.springframework.data.repository.CrudRepository;

public interface OrderItemRepository extends CrudRepository<OrderItem, OrderItemId> {
}
