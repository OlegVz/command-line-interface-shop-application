package com.hybris.shop.mapper;

import com.hybris.shop.dto.NewOrderDto;
import com.hybris.shop.dto.OrderDto;
import com.hybris.shop.model.Order;
import com.hybris.shop.model.User;
import com.hybris.shop.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Optional;

@Component
public class OrderMapper {

    private ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Autowired
    public OrderMapper(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
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

    public Order toEntityFromOrderDto(OrderDto orderDto) {
        return Objects.isNull(orderDto) ? null : modelMapper.map(orderDto, Order.class);
    }

    public Order toEntityFromNewOrderDto(NewOrderDto newOrderDto) {
        return Objects.isNull(newOrderDto) ? null : modelMapper.map(newOrderDto, Order.class);
    }
}
