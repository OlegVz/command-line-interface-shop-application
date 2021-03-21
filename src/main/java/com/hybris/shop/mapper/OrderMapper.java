package com.hybris.shop.mapper;

import com.hybris.shop.dto.NewOrderDto;
import com.hybris.shop.dto.OrderDto;
import com.hybris.shop.dto.UserOrdersDto;
import com.hybris.shop.model.Order;
import com.hybris.shop.model.OrderItem;
import com.hybris.shop.model.User;
import com.hybris.shop.repository.OrderItemRepository;
import org.modelmapper.ModelMapper;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//@Component
public class OrderMapper {

    private final ModelMapper modelMapper;
    private final OrderItemRepository orderItemRepository;

//    @Autowired
    public OrderMapper(ModelMapper modelMapper,
                       OrderItemRepository orderItemRepository) {
        this.modelMapper = modelMapper;
        this.orderItemRepository = orderItemRepository;
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

                    return destination;
                }
        );

        modelMapper.createTypeMap(Order.class, UserOrdersDto.class).setPostConverter(
                context -> {
                    Order source = context.getSource();
                    UserOrdersDto destination = context.getDestination();

                    List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(source.getId());

                    List<String> productsNames = orderItems.stream()
                            .map(orderItem -> orderItem.getProduct().getName())
                            .collect(Collectors.toList());

                    List<Integer> prices = orderItems.stream()
                            .map(orderItem -> orderItem.getProduct().getPrice())
                            .collect(Collectors.toList());

                    List<Integer> quantityList = orderItems.stream()
                            .map(OrderItem::getQuantity)
                            .collect(Collectors.toList());

                    ArrayList<Integer> totalPrices = new ArrayList<>();

                    for (int i = 0; i < productsNames.size(); i++) {
                        totalPrices.add(prices.get(i) * quantityList.get(i));
                    }

                    destination.setId(source.getId());
                    destination.setProductNames(productsNames);
                    destination.setQuantity(quantityList);
                    destination.setProductTotalPrice(totalPrices);
                    destination.setCreatedAt(source.getCreatedAt());

                    return destination;
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
