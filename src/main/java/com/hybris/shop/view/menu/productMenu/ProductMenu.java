package com.hybris.shop.view.menu.productMenu;

import com.hybris.shop.dto.NewProductDto;
import com.hybris.shop.dto.ProductDto;
import com.hybris.shop.facade.impl.ProductFacade;
import com.hybris.shop.model.Product;
import com.hybris.shop.view.console.Input;
import com.hybris.shop.view.console.Printer;
import com.hybris.shop.view.menu.commands.CommandsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static com.hybris.shop.view.console.Input.command;
import static com.hybris.shop.view.menu.commands.CommandsValidator.*;

@Component
public class ProductMenu {

    private final Printer printer;
    private final Input input;

    private final ProductFacade productFacade;

    @Autowired
    public ProductMenu(Printer printer, Input input, ProductFacade productFacade) {
        this.printer = printer;
        this.input = input;
        this.productFacade = productFacade;
    }

    public void menu() {
        do {
            printProductMenu();

            command = input.getCommand();
            if (isExitCommand(command) || isBAckCommand(command)) {
                return;
            }

            switch (command) {
                case "1":
                    saveNewProduct();
                    break;
                case "2":
                    break;
                case "3":
                    break;
                case "4":
                    break;
                default:
                    printer.printLine("Invalid command: " + command + "\n");
            }

            if (isExitCommand(command) || isBAckCommand(command)) {
                return;
            }
        } while (true);
    }

    private void saveNewProduct() {
        NewProductDto newProductDto = new NewProductDto();

        printer.printLine("New product menu\n");

        String productName = setProductName();

        if (CommandsValidator.confirmCommand("Save product name?")) {
            newProductDto.setName(productName);
        }

        Integer productPrice = setProductPrice();

        if (CommandsValidator.confirmCommand("Save product price?")) {
            newProductDto.setPrice(productPrice);
        }


        Product.ProductStatus productStatus = setProductStatus();

        if (isExitCommand(command) || isBAckCommand(command)) {
            return;
        }

        newProductDto.setStatus(productStatus);

        LocalDateTime createdAt = setLocalDateTime();
        newProductDto.setCreatedAt(createdAt);

        ProductDto savedProduct = productFacade.save(newProductDto);

        printer.printTable(List.of(savedProduct));
    }

    private LocalDateTime setLocalDateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return LocalDateTime.parse(LocalDateTime.now().format(dateTimeFormatter), dateTimeFormatter);
    }

    private Product.ProductStatus setProductStatus() {

        do {
            printProductStatusMenu();

            command = input.getCommand();
            if (isExitCommand(command) || isBAckCommand(command)) {
                return null;
            }

            switch (command) {
                case "1":
                    return Product.ProductStatus.IN_STOCK;
                case "2":
                    return Product.ProductStatus.RUNNING_LOW;
                case "3":
                    return Product.ProductStatus.OUT_OF_STOCK;
                default:
                    printer.printLine("Invalid command: " + command + "\n");
            }

        } while (true);
    }

    private String setProductName() {
        boolean b;

        do {
            printer.printLine("Product name\n");

            command = input.getCommand();
            if (isExitCommand(command) || isBAckCommand(command)) {
                return command;
            }
            b = productFacade.existsByName(command);

            if (b) {
                printer.printLine(String.format("Product with such name '%s' already exist. Please chose another name",
                        command));
            }
        } while (b);

        return command;
    }

    private Integer setProductPrice() {
        Integer productPrice = null;

        do {
            try {
                printer.printLine("Product price\n");
                command = input.getCommand();
                if (isExitCommand(command) || isBAckCommand(command)) {
                    break;
                }

                productPrice = Integer.parseInt(command);

                if (productPrice <= 0) {
                    throw new IllegalArgumentException();
                }

            } catch (NumberFormatException ex) {
                printer.printLine(String.format("Invalid input '%s'. Please input only numbers!\n", command));
            } catch (IllegalArgumentException ex) {
                printer.printLine("Price mast be bigger then 0!\n");
                productPrice = null;
            }
        } while (Objects.isNull(productPrice));

        return productPrice;
    }

    private void printProductMenu() {
        printer.printLine("Product menu\n");
        printer.printLine("\t- to create new product press '1';\n");
        printer.printLine("\t- to update product press '2';\n");
        printer.printLine("\t- to delete product '3';\n");
        printer.printLine("\t- to delete all products press '4';\n");
        printer.printLine("Back to previous menu input 'back'\n");
        printer.printLine("Exit from program input 'exit'\n");
    }

    private void printProductStatusMenu() {
        printer.printLine("Select product status\n");
        printer.printLine("\t- select 'IN_STOCK' press '1';\n");
        printer.printLine("\t- select 'RUNNING_LOW' press '2';\n");
        printer.printLine("\t- select 'OUT_OF_STOCK' press '3';\n");
        printer.printLine("Back to previous menu input 'back'\n");
        printer.printLine("Exit from program input 'exit'\n");
    }
}
