package com.hybris.shop.mapper;

import com.hybris.shop.dto.NewOrderDto;
import com.hybris.shop.dto.OrderDto;
import com.hybris.shop.model.Order;
import com.hybris.shop.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

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
    }

    public OrderDto toOrderDtoFromEntity(Order order) {
        return Objects.isNull(order) ? null : modelMapper.map(order, OrderDto.class);
    }

    public Order toEntityFromNewOrderDto(NewOrderDto newOrderDto) {
        return Objects.isNull(newOrderDto) ? null : modelMapper.map(newOrderDto, Order.class);
    }
}
