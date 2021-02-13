package com.hybris.shop.view.menu.orderMenu;

import com.hybris.shop.dto.*;
import com.hybris.shop.exceptions.productExceptions.ProductNotFoundByIdException;
import com.hybris.shop.exceptions.productExceptions.ProductOutOfStockException;
import com.hybris.shop.facade.impl.OrderFacade;
import com.hybris.shop.facade.impl.OrderItemFacade;
import com.hybris.shop.facade.impl.ProductFacade;
import com.hybris.shop.facade.impl.UserFacade;
import com.hybris.shop.mapper.OrderMapper;
import com.hybris.shop.model.Product;
import com.hybris.shop.view.console.Input;
import com.hybris.shop.view.console.Printer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hybris.shop.view.console.Input.command;
import static com.hybris.shop.view.menu.commands.CommandsValidator.*;
import static com.hybris.shop.view.menu.userMenu.UserMenu.currentUserId;

@Component
public class OrderMenu {

    private OrderFacade orderFacade;
    private OrderMapper orderMapper;
    private final Printer printer;
    private final Input input;
    private final ProductFacade productFacade;
    private final OrderItemFacade orderItemFacade;
    private final UserFacade userFacade;

    @Autowired
    public OrderMenu(OrderFacade orderFacade,
                     OrderMapper orderMapper,
                     Printer printer,
                     Input input,
                     ProductFacade productFacade,
                     OrderItemFacade orderItemFacade,
                     UserFacade userFacade) {
        this.orderFacade = orderFacade;
        this.orderMapper = orderMapper;
        this.printer = printer;
        this.input = input;
        this.productFacade = productFacade;
        this.orderItemFacade = orderItemFacade;
        this.userFacade = userFacade;
    }

    public void menu() {
        do {
            printOrderMenu();

            command = input.getCommand();
            if (isExitCommand(command) || isBAckCommand(command)) {
                return;
            }

            switch (command) {
                case "1":
                    doOrder();
                    break;
                case "2":
                    System.out.println("Function in progress");
                    break;
                case "3":
                    if (confirmCommand("Confirm delete order?")) {
                    }
                    break;
                default:
                    printer.printLine("Invalid command: " + command + "\n");
            }

            if (isExitCommand(command)) {
                return;
            }
        } while (true);
    }

    private void doOrder() {
        NewOrderDto newOrderDto = new NewOrderDto();
        NewOrderItemDto newOrderItemDto = new NewOrderItemDto();
        OrderDto orderDto;
        Map<Long, Integer> productToOrder = new HashMap<>();
        Long orderId;

       do {
            Long productId;
            Integer productQuantity;

            List<ProductDto> productDtoList = productFacade.findAll().stream()
                    .filter(productDto -> !productDto.getStatus().equals(Product.ProductStatus.OUT_OF_STOCK))
                    .collect(Collectors.toList());

            printer.printLine("Product list\n");
            printer.printTable(productDtoList);

            productId = setProductId();

            if (isExitCommand(command) || isBAckCommand(command)) {
                return;
            }

            productQuantity = setProductQuantity();

            if (isExitCommand(command) || isBAckCommand(command)) {
                return;
            }

            productToOrder.put(productId, productQuantity);

        } while (confirmCommand("Do you want add new product to the order?"));

       if (confirmCommand("Do you confirm order?")) {
           newOrderDto.setUserId(currentUserId);
           orderDto = orderFacade.save(newOrderDto);
           orderId = orderDto.getId();

           productToOrder.forEach((productId, quantity) -> {
               newOrderItemDto.setOrderId(orderId);
               newOrderItemDto.setProductId(productId);
               newOrderItemDto.setQuantity(quantity);
               orderItemFacade.save(newOrderItemDto);
           });

           List<UserOrdersDto> userOrderDtos = userFacade.findAllUserOrders(currentUserId).stream()
                   .filter(userOrdersDto -> userOrdersDto.getId().equals(orderId))
                   .collect(Collectors.toList());

           printer.printTable(userOrderDtos);
       }
    }

    private Integer setProductQuantity() {
        Integer productQuantity = null;
        boolean b = false;

        do {
            try {
                printer.printLine("Select product quantity\n");
                command = input.getCommand();
                if (isExitCommand(command) || isBAckCommand(command)) {
                    break;
                }

                productQuantity = Integer.parseInt(command);

                if (productQuantity <= 0) {
                    throw new IllegalArgumentException();
                }
            } catch (NumberFormatException ex) {
                printer.printLine(String.format("Invalid input '%s'. Please input only numbers!\n", command));
            } catch (IllegalArgumentException ex) {
                printer.printLine("Quantity mast be bigger then 0!\n");
                productQuantity = null;
            }

        } while (Objects.isNull(productQuantity));

        return productQuantity;
    }

    private Long setProductId() {
        Long productId = null;
        boolean b = false;

        do {
            try {
                printer.printLine("Select product id to order\n");
                command = input.getCommand();
                if (isExitCommand(command) || isBAckCommand(command)) {
                    break;
                }

                productId = Long.parseLong(command);

                ProductDto productById = productFacade.findById(productId);

                if (productById.getStatus().equals(Product.ProductStatus.OUT_OF_STOCK)) {
                    throw new ProductOutOfStockException(productById.toString());
                }

                b = productFacade.existsById(productId);
            } catch (NumberFormatException ex) {
                printer.printLine(String.format("Invalid input '%s'. Please input only numbers!\n", command));
            } catch (ProductNotFoundByIdException ex) {
                printer.printLine(String.format("Product with id '%s' not fount. Please input id from product table!\n",
                        command));
            } catch (ProductOutOfStockException ex) {
                printer.printLine("Please input product id from table\n");
            }

        } while (!b);

        return productId;
    }

    private void printOrderMenu() {
        printer.printLine("Order menu\n");
        printer.printLine("\t- to do new order press '1';\n");
        printer.printLine("\t- to update order status press '2';\n");
        printer.printLine("\t- to delete order press '3';\n");
        printer.printLine("Back to previous menu input 'back'\n");
        printer.printLine("Exit from program input 'exit'\n");
    }
}
