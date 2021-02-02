package com.hybris.shop.service.impl.integration;

import com.hybris.shop.ShopApplication;
import com.hybris.shop.model.Order;
import com.hybris.shop.model.User;
import com.hybris.shop.service.impl.OrderService;
import com.hybris.shop.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {ShopApplication.class})
public class OrderServiceTestIT {

    private static final String ORDER_STATUS = "Order status";
    private static final String CREATED_AT = "2021-01-31";

    private Order order;
    private User userInDb;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void init() {
        userInDb = userService.save(new User());

        order = new Order();
        order.setUser(this.userInDb);
        order.setStatus(ORDER_STATUS);
        order.setCreatedAt(CREATED_AT);
    }

    @Test
    void shouldSaveNewOrder() {
        //given
        //when
        Order savedOrder = orderService.save(order);

        //then
        assertNotNull(savedOrder.getId());
        assertEquals(userInDb.getId(), savedOrder.getUser().getId());
        assertEquals(ORDER_STATUS, savedOrder.getStatus());
        assertEquals(CREATED_AT, savedOrder.getCreatedAt());
    }
}
