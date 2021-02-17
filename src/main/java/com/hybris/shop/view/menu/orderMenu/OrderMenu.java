package com.hybris.shop.view.menu.orderMenu;

import com.hybris.shop.dto.*;
import com.hybris.shop.exceptions.orderExceptions.OrderNotFoundByIdException;
import com.hybris.shop.exceptions.productExceptions.ProductNotFoundByIdException;
import com.hybris.shop.exceptions.productExceptions.ProductOutOfStockException;
import com.hybris.shop.facade.impl.OrderFacade;
import com.hybris.shop.facade.impl.OrderItemFacade;
import com.hybris.shop.facade.impl.ProductFacade;
import com.hybris.shop.facade.impl.UserFacade;
import com.hybris.shop.model.Order;
import com.hybris.shop.model.Product;
import com.hybris.shop.view.consoleInputOutput.Input;
import com.hybris.shop.view.consoleInputOutput.Printer;
import com.hybris.shop.view.menu.userMenu.UserMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hybris.shop.view.consoleInputOutput.Input.command;
import static com.hybris.shop.view.menu.commands.CommandsValidator.*;
import static com.hybris.shop.view.menu.userMenu.UserMenu.currentUserId;

@Component
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderMenu {

    private final Printer printer;
    private final Input input;

    private final OrderFacade orderFacade;
    private final ProductFacade productFacade;
    private final OrderItemFacade orderItemFacade;
    private final UserFacade userFacade;

    private final UserMenu userMenu;

    @Autowired
    public OrderMenu(OrderFacade orderFacade,
                     Printer printer,
                     Input input,
                     ProductFacade productFacade,
                     OrderItemFacade orderItemFacade,
                     UserFacade userFacade,
                     UserMenu userMenu) {
        this.orderFacade = orderFacade;
        this.printer = printer;
        this.input = input;
        this.productFacade = productFacade;
        this.orderItemFacade = orderItemFacade;
        this.userFacade = userFacade;
        this.userMenu = userMenu;
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
                    updateOrderStatus();
                    break;
                case "3":
                    deleteOrder();
                    break;
                case "4":
                    listAllOrders();
                    break;
                default:
                    printer.printLine("Invalid command: " + command + "\n");
            }

            if (isExitCommand(command)) {
                return;
            }
        } while (true);
    }

    private void listAllOrders() {
        printer.printLine("All orders list:\n");

        List<UserOrdersDto> allOrdersWitProducts = orderFacade.findAllOrdersWitProducts();

        if (allOrdersWitProducts.isEmpty()) {
            printer.printLine("No available orders\n");
            return;
        }

        printer.printTable(allOrdersWitProducts);
    }

    private void updateOrderStatus() {
        printUpdateOrderMenu();

        NewOrderDto newOrderDto = new NewOrderDto();
        userMenu.printListOfUserOrders();

        if (isBAckCommand(command)) {
            return;
        }

        Long orderId = setOrderId();
        if (isExitCommand(command) || isBAckCommand(command)) {
            return;
        }

        do {
            command = input.getCommand();
            if (isExitCommand(command) || isBAckCommand(command)) {
                return;
            }

            switch (command) {
                case "1":
                    newOrderDto.setStatus(Order.OrderStatus.CONFIRMED_ORDER.getStatus());
                    break;
                case "2":
                    newOrderDto.setStatus(Order.OrderStatus.FULFILLED_ORDER.getStatus());
                    break;
                default:
                    printer.printLine("Invalid command: " + command + "\n");
            }
        } while (Objects.isNull(newOrderDto.getStatus()));

        if (confirmCommand("Save changes?")) {
            OrderDto updateOrder = orderFacade.update(orderId, newOrderDto);
            printer.printLine("New order data\n");
            printer.printTable(List.of(updateOrder));
        }
    }

    private void deleteOrder() {
        userMenu.printListOfUserOrders();

        if (isBAckCommand(command)) {
            return;
        }

        Long orderId = setOrderId();

        if (isExitCommand(command) || isBAckCommand(command)) {
            return;
        }

        OrderDto orderById = orderFacade.findById(orderId);

        printer.printLine("Order to remove:\n");
        printer.printTable(List.of(orderById));

        if (confirmCommand("Remove order?")) {
            orderFacade.deleteById(orderId);
            printer.printLine("Order removed\n");
        }
    }

    private Long setOrderId() {
        Long orderId = null;
        boolean isOrderExist = false;

        do {
            try {
                printer.printLine("Select order id\n");

                command = input.getCommand();
                if (isExitCommand(command) || isBAckCommand(command)) {
                    break;
                }

                orderId = Long.parseLong(command);

                isOrderExist = orderFacade.existsById(orderId);
                if (!isOrderExist) {
                    throw new OrderNotFoundByIdException(orderId);
                }
            } catch (NumberFormatException ex) {
                printer.printLine(String.format("Invalid input '%s'. Please input only numbers!\n", command));
            } catch (OrderNotFoundByIdException ex) {
                printer.printLine(String.format("Order with id '%s' not fount. Please input id from product table!\n",
                        command));
            }
        } while (!isOrderExist);

        return orderId;
    }

    private void doOrder() {
        Map<Long, Integer> productToOrder = new HashMap<>();
        NewOrderDto newOrderDto = new NewOrderDto();
        NewOrderItemDto newOrderItemDto = new NewOrderItemDto();
        OrderDto orderDto;
        Long orderId;

        do {
            Long productId;
            Integer productQuantity;

            List<ProductDto> productDtoList = productFacade.findAll().stream()
                    .filter(productDto -> !productDto.getStatus().equals(Product.ProductStatus.OUT_OF_STOCK))
                    .collect(Collectors.toList());

            if (productDtoList.isEmpty()) {
                printer.printLine("No available products!");
                return;
            }

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

            List<UserOrdersDto> allUserOrders = userFacade.findAllUserOrders(currentUserId);
            List<UserOrdersDto> userOrderDtos = allUserOrders.stream()
                    .filter(userOrdersDto -> userOrdersDto.getId().equals(orderId))
                    .collect(Collectors.toList());

            printer.printTable(userOrderDtos);
        }
    }

    private Integer setProductQuantity() {
        Integer productQuantity = null;

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
        printer.printLine("\t- to list all orders press '4';\n");
        printer.printLine("Back to previous menu input 'back'\n");
        printer.printLine("Exit from program input 'exit'\n");
    }

    private void printUpdateOrderMenu() {
        printer.printLine("Update order status menu\n");
        printer.printLine(" - to change order status on 'Confirmed order' press '1'\n");
        printer.printLine(" - to change order status on 'Ful filled order' press '2'\n");
        printer.printLine("Back to previous menu input 'back'\n");
        printer.printLine("Exit from program input 'exit'\n");
    }
}
