package com.hybris.shop.mapper;

import com.hybris.shop.dto.NewOrderItemDto;
import com.hybris.shop.dto.OrderItemDto;
import com.hybris.shop.dto.orderItemDtosId.OrderItemDtoId;
import com.hybris.shop.model.Order;
import com.hybris.shop.model.OrderItem;
import com.hybris.shop.model.Product;
import com.hybris.shop.model.idClasses.OrderItemId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class OrderItemMapper {

    private ModelMapper modelMapper;

    @Autowired
    public OrderItemMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(NewOrderItemDto.class, OrderItem.class).setPostConverter(
                context -> {
                    NewOrderItemDto source = context.getSource();
                    OrderItem destination = context.getDestination();

                    Order order = new Order();
                    order.setId(source.getOrderId());

                    Product product = new Product();
                    product.setId(source.getProductId());

                    destination.setOrder(order);
                    destination.setProduct(product);

                    return context.getDestination();
                }
        );
    }

    public OrderItem toEntityFromNewOrderItemDto(NewOrderItemDto newOrderItemDto) {
        return Objects.isNull(newOrderItemDto) ? null : modelMapper.map(newOrderItemDto, OrderItem.class);
    }

    public OrderItemDto toOrderItemDtoFromEntity(OrderItem orderItem) {
        return Objects.isNull(orderItem) ? null : modelMapper.map(orderItem, OrderItemDto.class);
    }

    public OrderItemId fromOrderItemDtoIdToOrderItemId(OrderItemDtoId orderItemDtoId) {
        return Objects.isNull(orderItemDtoId) ? null : modelMapper.map(orderItemDtoId, OrderItemId.class);
    }
}
