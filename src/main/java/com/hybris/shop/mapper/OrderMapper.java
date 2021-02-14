package com.hybris.shop.mapper;

import com.hybris.shop.dto.NewOrderDto;
import com.hybris.shop.dto.OrderDto;
import com.hybris.shop.dto.UserOrdersDto;
import com.hybris.shop.model.Order;
import com.hybris.shop.model.OrderItem;
import com.hybris.shop.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public OrderMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(NewOrderDto.class, Order.class).setPostConverter(
                context -> {
                    NewOrderDto source = context.getSource();
                    Order destination = context.getDestination();

                    User user = new User();
                    user.setId(source.getUserId());

                    destination.setUser(user);

                    return context.getDestination();
                }
        );

        modelMapper.createTypeMap(Order.class, UserOrdersDto.class).setPostConverter(
                context -> {
                    Order source = context.getSource();
                    UserOrdersDto destination = context.getDestination();

                    List<String> productsNames = source.getOrderItems().stream()
                            .map(orderItem -> orderItem.getProduct().getName())
                            .collect(Collectors.toList());

                    List<Integer> prices = source.getOrderItems().stream()
                            .map(orderItem -> orderItem.getProduct().getPrice())
                            .collect(Collectors.toList());

                    List<Integer> quantityList = source.getOrderItems().stream()
                            .map(OrderItem::getQuantity)
                            .collect(Collectors.toList());

                    ArrayList<Integer> totalPrices = new ArrayList<>();

                    for (int i = 0; i < productsNames.size(); i++) {
                        totalPrices.add(prices.get(i) * quantityList.get(i));
                    }

                    destination.setProductNames(productsNames);
                    destination.setQuantity(quantityList);
                    destination.setProductTotalPrice(totalPrices);

                    return context.getDestination();
                }
        );
    }

    public OrderDto toOrderDtoFromEntity(Order order) {
        return Objects.isNull(order) ? null : modelMapper.map(order, OrderDto.class);
    }

    public Order toEntityFromNewOrderDto(NewOrderDto newOrderDto) {
        return Objects.isNull(newOrderDto) ? null : modelMapper.map(newOrderDto, Order.class);
    }

    public UserOrdersDto toUserOrdersDto(Order order) {
        return Objects.isNull(order) ? null : modelMapper.map(order, UserOrdersDto.class);
    }
}
