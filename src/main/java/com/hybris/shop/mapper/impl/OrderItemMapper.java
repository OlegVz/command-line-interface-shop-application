package com.hybris.shop.mapper.impl;

import com.hybris.shop.dto.NewOrderItemDto;
import com.hybris.shop.dto.OrderItemDto;
import com.hybris.shop.dto.orderItemDtosId.OrderItemDtoId;
import com.hybris.shop.mapper.OrderItemMapperInterface;
import com.hybris.shop.mapper.OrderMapperInterface;
import com.hybris.shop.mapper.ProductMapperInterface;
import com.hybris.shop.model.Order;
import com.hybris.shop.model.OrderItem;
import com.hybris.shop.model.Product;
import com.hybris.shop.model.idClasses.OrderItemId;
import com.hybris.shop.repository.OrderItemRepository;
import com.hybris.shop.repository.OrderRepository;
import com.hybris.shop.repository.ProductRepository;
import org.modelmapper.ModelMapper;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

//@Component
public class OrderItemMapper implements OrderItemMapperInterface {

    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductMapperInterface productMapperInterface;
    private final OrderMapperInterface orderMapperInterface;

//    @Autowired
    public OrderItemMapper(ModelMapper modelMapper,
                           OrderRepository orderRepository,
                           ProductRepository productRepository,
                           OrderItemRepository orderItemRepository,
                           ProductMapperInterface productMapperInterface,
                           OrderMapperInterface orderMapperInterface) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.productMapperInterface = productMapperInterface;
        this.orderMapperInterface = orderMapperInterface;
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(NewOrderItemDto.class, OrderItem.class).setPostConverter(
                context -> {
                    NewOrderItemDto source = context.getSource();
                    OrderItem destination = context.getDestination();

                    Optional<Order> orderById = orderRepository.findById(source.getOrderId());
                    orderById.ifPresent(destination::setOrder);

                    Optional<Product> productById = productRepository.findById(source.getProductId());
                    productById.ifPresent(destination::setProduct);

                    return destination;
                }
        );

        modelMapper.createTypeMap(OrderItem.class, OrderItemDto.class).setPostConverter(
                context -> {
                    OrderItem source = context.getSource();
                    OrderItemDto destination = context.getDestination();

                    List<OrderItem> allByProductId = orderItemRepository.findAllByProductId(source.getProduct().getId());
                    List<OrderItem> allByOrderId = orderItemRepository.findAllByOrderId(source.getOrder().getId());
                    source.getProduct().setOrderItems(allByProductId);
                    source.getOrder().setOrderItems(allByOrderId);

                    destination.setProduct(productMapperInterface.fromEntityToProductDto(source.getProduct()));
                    destination.setOrder(orderMapperInterface.toOrderDtoFromEntity(source.getOrder()));
                    destination.setQuantity(source.getQuantity());

                    return destination;
                }
        );
    }

    @Override
    public OrderItem toEntityFromNewOrderItemDto(NewOrderItemDto newOrderItemDto) {
        return Objects.isNull(newOrderItemDto) ? null : modelMapper.map(newOrderItemDto, OrderItem.class);
    }

    @Override
    public OrderItemDto toOrderItemDtoFromEntity(OrderItem orderItem) {
        return Objects.isNull(orderItem) ? null : modelMapper.map(orderItem, OrderItemDto.class);
    }

    @Override
    public OrderItemId fromOrderItemDtoIdToOrderItemId(OrderItemDtoId orderItemDtoId) {
        return Objects.isNull(orderItemDtoId) ? null : modelMapper.map(orderItemDtoId, OrderItemId.class);
    }
}
