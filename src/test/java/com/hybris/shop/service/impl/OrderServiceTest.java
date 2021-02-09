package com.hybris.shop.service.impl;

import com.hybris.shop.exceptions.orderExceptions.OrderNotFoundByIdException;
import com.hybris.shop.exceptions.userExceptions.UserNotFoundByIdException;
import com.hybris.shop.model.Order;
import com.hybris.shop.model.User;
import com.hybris.shop.repository.OrderRepository;
import com.hybris.shop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private static final long ORDER_ID = 1L;
    private static final long NOT_EXISTING_ORDER_ID = 0L;
    private static final long USER_ID = 2L;
    private static final long NEW_USER_ID = 3L;
    private static final long NOT_EXISTING_USER_ID = 0L;
    private static final String ORDER_STATUS = "Order status";
    private static final String CREATED_AT = "2021-01-31";
    private static final String NEW_ORDER_STATUS = "New order status";
    private static final String NEW_CREATED_AT = "2021-02-01";

    private Order orderInDb;
    private Order newOrder;
    private User userWithNotExistingId;

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        userWithNotExistingId = new User();
        userWithNotExistingId.setId(NOT_EXISTING_USER_ID);

        User userInDb = new User();
        userInDb.setId(USER_ID);

        orderInDb = new Order();
        orderInDb.setId(ORDER_ID);
        orderInDb.setUser(userInDb);
        orderInDb.setStatus(ORDER_STATUS);
        orderInDb.setCreatedAt(CREATED_AT);

        User newUser = new User();
        newUser.setId(NEW_USER_ID);

        newOrder = new Order();
        newOrder.setUser(newUser);
        newOrder.setStatus(NEW_ORDER_STATUS);
        newOrder.setCreatedAt(NEW_CREATED_AT);
    }

    @Test
    void shouldThrowExceptionWhenTrySaveOrderWithNotExistingUser() {
        //given
        newOrder.setUser(userWithNotExistingId);

        //when
        when(userRepository.existsById(anyLong())).thenReturn(false);

        //then
        UserNotFoundByIdException exception =
                assertThrows(UserNotFoundByIdException.class, () -> orderService.save(newOrder));

        assertEquals(String.format("User with id %s was not found", NOT_EXISTING_USER_ID), exception.getMessage());
    }

    @Test
    void shouldFindAndReturnOrderById() {
        //given
        //when
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderInDb));

        Order orderById = orderService.findById(ORDER_ID);

        //then
        assertEquals(ORDER_ID, orderById.getId());
    }

    @Test
    void shouldThrowExceptionWhenFindOrderByNotExistId() {
        //given
        //when
        when(orderRepository.findById(anyLong())).thenThrow(new OrderNotFoundByIdException(NOT_EXISTING_ORDER_ID));

        //then
        OrderNotFoundByIdException exception =
                assertThrows(OrderNotFoundByIdException.class, () -> orderService.findById(NOT_EXISTING_ORDER_ID));

        assertEquals(String.format("Order with id %s was not found", NOT_EXISTING_ORDER_ID), exception.getMessage());
    }

    @Test
    void shouldUpdateOrderData() {
        //given
        //when
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderInDb));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(orderService.save(orderInDb)).thenReturn(orderInDb);

        Order updatedOrder = orderService.update(ORDER_ID, newOrder);

        //then
        assertEquals(ORDER_ID, updatedOrder.getId());
        assertEquals(NEW_USER_ID, updatedOrder.getUser().getId());
        assertEquals(NEW_ORDER_STATUS, updatedOrder.getStatus());
        assertEquals(NEW_CREATED_AT, updatedOrder.getCreatedAt());
    }

    @Test
    void shouldUpdateOnlyOrderUserID() {
        //given
        newOrder.setStatus(null);
        newOrder.setCreatedAt(null);

        //when
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderInDb));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(orderService.save(orderInDb)).thenReturn(orderInDb);

        Order updatedOrder = orderService.update(ORDER_ID, newOrder);

        //then
        assertEquals(ORDER_ID, updatedOrder.getId());
        assertEquals(NEW_USER_ID, updatedOrder.getUser().getId());
        assertEquals(ORDER_STATUS, updatedOrder.getStatus());
        assertEquals(CREATED_AT, updatedOrder.getCreatedAt());
    }

    @Test
    void shouldUpdateOnlyOrderStatus() {
        //given
        newOrder.setUser(null);
        newOrder.setCreatedAt(null);

        //when
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderInDb));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(orderService.save(orderInDb)).thenReturn(orderInDb);

        Order updatedOrder = orderService.update(ORDER_ID, newOrder);

        //then
        assertEquals(ORDER_ID, updatedOrder.getId());
        assertEquals(USER_ID, updatedOrder.getUser().getId());
        assertEquals(NEW_ORDER_STATUS, updatedOrder.getStatus());
        assertEquals(CREATED_AT, updatedOrder.getCreatedAt());
    }

    @Test
    void shouldUpdateOnlyOrderCreationDate() {
        //given
        newOrder.setUser(null);
        newOrder.setStatus(null);

        //when
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(orderInDb));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(orderService.save(orderInDb)).thenReturn(orderInDb);

        Order updatedOrder = orderService.update(ORDER_ID, newOrder);

        //then
        assertEquals(ORDER_ID, updatedOrder.getId());
        assertEquals(USER_ID, updatedOrder.getUser().getId());
        assertEquals(ORDER_STATUS, updatedOrder.getStatus());
        assertEquals(NEW_CREATED_AT, updatedOrder.getCreatedAt());
    }

    @Test
    void shouldReturnTrueWhenExistById() {
        //given
        //when
        when(orderRepository.existsById(anyLong())).thenReturn(true);

        boolean b = orderService.existsById(ORDER_ID);

        //then
        assertTrue(b);
    }

    @Test
    void shouldReturnFalseWhenExistById() {
        //given
        //when
        when(orderRepository.existsById(anyLong())).thenReturn(false);

        boolean b = orderService.existsById(NOT_EXISTING_ORDER_ID);

        //then
        assertFalse(b);
    }

    @Test
    void shouldDeleteOrderById() {
        //given
        //when
        when(orderRepository.existsById(anyLong())).thenReturn(true);

        orderService.deleteById(ORDER_ID);

        //then
        verify(orderRepository, times(1)).deleteById(ORDER_ID);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldThrowExceptionWhenDeleteOrderByIdWithNotExistingId() {
        //given
        //when
        when(orderRepository.existsById(anyLong())).thenReturn(false);

        //then
        OrderNotFoundByIdException exception =
                assertThrows(OrderNotFoundByIdException.class, () -> orderService.deleteById(NOT_EXISTING_ORDER_ID));

        assertEquals(String.format("Order with id %s was not found", NOT_EXISTING_ORDER_ID), exception.getMessage());
    }

    @Test
    void shouldFindAndReturnAllOrdersInDb() {
        //given
        List<Order> ordersInDb = List.of(this.orderInDb);
        Iterable<Order> orderIterable = ordersInDb;

        //when
        when(orderRepository.findAll()).thenReturn(orderIterable);

        List<Order> orders = orderService.findAll();

        //then
        for (int i = 0; i < orders.size(); i++) {
            assertEquals(ordersInDb.get(i), orders.get(i));
        }
    }
}