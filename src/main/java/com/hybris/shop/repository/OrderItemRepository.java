package com.hybris.shop.repository;

import com.hybris.shop.model.OrderItem;
import com.hybris.shop.model.idClasses.OrderItemId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderItemRepository extends CrudRepository<OrderItem, OrderItemId> {
    List<OrderItem> findAllByProductId(Long productId);

    List<OrderItem> findAllByOrderId(Long orderId);
}
