package com.hybris.shop.view.menu.productMenu;

import com.hybris.shop.dto.NewProductDto;
import com.hybris.shop.dto.ProductDto;
import com.hybris.shop.exceptions.productExceptions.ProductNotFoundByIdException;
import com.hybris.shop.facade.impl.ProductFacade;
import com.hybris.shop.model.Product;
import com.hybris.shop.view.consoleInputOutput.InputInterface;
import com.hybris.shop.view.consoleInputOutput.PrinterInterface;
import com.hybris.shop.view.menu.MenuInterface;
import com.hybris.shop.view.menu.commands.CommandsValidator;
import com.hybris.shop.view.menu.userMenu.UserMenuInterface;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static com.hybris.shop.view.consoleInputOutput.impl.Input.command;
import static com.hybris.shop.view.menu.commands.CommandsValidator.*;

//@Component
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProductMenu implements MenuInterface {

    private final PrinterInterface printer;
    private final InputInterface inputInterface;

    private final ProductFacade productFacade;

    private final UserMenuInterface userMenuInterface;

//    @Autowired
    public ProductMenu(PrinterInterface printer,
                       InputInterface inputInterface,
                       ProductFacade productFacade,
                       UserMenuInterface userMenuInterface) {
        this.printer = printer;
        this.inputInterface = inputInterface;
        this.productFacade = productFacade;
        this.userMenuInterface = userMenuInterface;
    }

    @Override
    public void menu() {
        do {
            printProductMenu();

            command = inputInterface.getCommand();
            if (isExitCommand(command) || isBAckCommand(command)) {
                return;
            }

            switch (command) {
                case "1":
                    saveNewProduct();
                    break;
                case "2":
                    updateProduct();
                    break;
                case "3":
                    deleteProduct();
                    break;
                case "4":
                    listAllOrderedProducts();
                    break;
                case "5":
                    listAllProducts();
                    break;
                case "6":
                    deleteAllProducts();
                    break;
                default:
                    printer.printLine("Invalid command: " + command + "\n");
            }

            if (isExitCommand(command)) {
                return;
            }
        } while (true);
    }

    private void updateProduct() {
        NewProductDto newProductDto = new NewProductDto();
        List<ProductDto> allProducts = productFacade.findAll();

        if (allProducts.isEmpty()) {
            printer.printLine("Product table is empty!\n");
            return;
        }

        printer.printTable(allProducts);

        Long productId = setProductId();

        if (isExitCommand(command) || isBAckCommand(command)) {
            return;
        }

        do {
            printUpdateProductMenu();

            command = inputInterface.getCommand();
            if (isExitCommand(command) || isBAckCommand(command)) {
                return;
            }

            switch (command) {
                case "1":
                    boolean b;

                    do {
                        printer.printLine("Input new product name\n");
                        command = inputInterface.getCommand();
                        if (isExitCommand(command) || isBAckCommand(command)) {
                            return;
                        }
                        b = productFacade.existsByName(command);
                        if (b) {
                            printer.printLine("Product with such name exist!\n");
                        } else {
                            newProductDto.setName(command);
                        }
                    } while (b);
                    break;
                case "2":
                    Integer productPrice = setProductPrice();
                    newProductDto.setPrice(productPrice);
                    if (isExitCommand(command) || isBAckCommand(command)) {
                        return;
                    }
                    break;
                case "3":
                    Product.ProductStatus productStatus = null;
                    printUpdateProductStatusMenu();

                    do {
                        command = inputInterface.getCommand();
                        if (isExitCommand(command) || isBAckCommand(command)) {
                            return;
                        }

                        switch (command) {
                            case "1":
                                productStatus = Product.ProductStatus.IN_STOCK;
                                break;
                            case "2":
                                productStatus = Product.ProductStatus.RUNNING_LOW;
                                break;
                            case "3":
                                productStatus = Product.ProductStatus.OUT_OF_STOCK;
                                break;
                            default:
                                printer.printLine("Invalid command: " + command + "\n");
                        }
                    } while (Objects.isNull(productStatus));

                    newProductDto.setStatus(productStatus);
                    break;
                case "4":
                    if (confirmCommand("Save changes?")) {
                        ProductDto update = productFacade.update(productId, newProductDto);
                        printer.printLine("New product data\n");
                        printer.printTable(List.of(update));
                        return;
                    }
                    break;
                default:
                    printer.printLine("Invalid command: " + command + "\n");
            }
        } while (true);
    }

    private void deleteAllProducts() {
        List<ProductDto> allProducts = productFacade.findAll();

        if (allProducts.isEmpty()) {
            printer.printLine("Product table is empty!\n");
            return;
        }

        if (confirmCommand("Delete all products?")) {
            do {
                printer.printLine("Input password\n");
                command = inputInterface.getCommand();

                if (isExitCommand(command) || isBAckCommand(command)) {
                    return;
                }

                if (userMenuInterface.isPasswordCorrect(command)) {
                    productFacade.deleteAll();
                    printer.printLine("All products removed\n");
                    return;
                } else {
                    printer.printLine("Invalid password!\n");
                }
            } while (true);
        }
    }

    private void listAllProducts() {
        printer.printLine("List of all products\n");

        List<ProductDto> allProducts = productFacade.findAll();
        printProductsList(allProducts);
    }

    private void listAllOrderedProducts() {
        printer.printLine("List all products, which have been ordered at least once, with total ordered\n" +
                "quantity sorted descending by the quantity\n");

        List<ProductDto> productDtos = productFacade.sortProductsByNumberOfOrdersDesc();
        printProductsList(productDtos);
    }

    private void printProductsList(List<ProductDto> productDtos) {
        if (productDtos.isEmpty()) {
            printer.printLine("Product table is empty!\n");
            return;
        }

        printer.printTable(productDtos);
    }

    private void deleteProduct() {
        printer.printLine("Delete product by ID\n");
        printer.printLine("Product list\n");
        List<ProductDto> allProducts = productFacade.findAll();

        if (allProducts.isEmpty()) {
            printer.printLine("Product table is empty!\n");
            return;
        }

        printProductsList(allProducts);

        Long productId = setProductId();

        if (isExitCommand(command) || isBAckCommand(command)) {
            return;
        }

        ProductDto productById = productFacade.findById(productId);

        printer.printLine("Product to remove:\n");
        printer.printTable(List.of(productById));

        if (confirmCommand("Remove product?")) {
            do {
                printer.printLine("Input password\n");
                command = inputInterface.getCommand();
                if (isExitCommand(command) || isBAckCommand(command)) {
                    return;
                }

                if (userMenuInterface.isPasswordCorrect(command)) {
                    productFacade.deleteById(productId);
                    printer.printLine("Product removed\n");
                    return;
                } else {
                    printer.printLine("Invalid password!\n");
                }
            } while (true);
        }
    }

    private Long setProductId() {
        Long productId = null;
        boolean isProductExist = false;

        do {
            try {
                printer.printLine("Select product id\n");

                command = inputInterface.getCommand();
                if (isExitCommand(command) || isBAckCommand(command)) {
                    break;
                }

                productId = Long.parseLong(command);
                isProductExist = productFacade.existsById(productId);

                if (!isProductExist) {
                    throw new ProductNotFoundByIdException(productId);
                }
            } catch (NumberFormatException ex) {
                printer.printLine(String.format("Invalid input '%s'. Please input only numbers!\n", command));
            } catch (ProductNotFoundByIdException ex) {
                printer.printLine(String.format("Product with id '%s' not fount. Please input id from product table!\n",
                        command));
            }
        } while (!isProductExist);

        return productId;
    }

    private void saveNewProduct() {
        NewProductDto newProductDto = new NewProductDto();

        printer.printLine("New product menu\n");

        String productName = setProductName();
        if (isExitCommand(command) || isBAckCommand(command)) {
            return;
        }
        if (CommandsValidator.confirmCommand("Save product name?")) {
            newProductDto.setName(productName);
        }

        Integer productPrice = setProductPrice();
        if (isExitCommand(command) || isBAckCommand(command)) {
            return;
        }
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

            command = inputInterface.getCommand();
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

            command = inputInterface.getCommand();
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
                command = inputInterface.getCommand();
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
        printer.printLine("\t- to list all products, which have been ordered at least once, with total ordered\n" +
                "\t  quantity sorted descending by the quantity press '4';\n");
        printer.printLine("\t- to list all products press '5';\n");
        printer.printLine("\t- to delete all products press '6';\n");
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

    private void printUpdateProductMenu() {
        printer.printLine("Update product menu\n");
        printer.printLine(" - to change product name press '1'\n");
        printer.printLine(" - to change product price press '2'\n");
        printer.printLine(" - to change product status press '3'\n");
        printer.printLine(" - to save changes press '4'\n");
        printer.printLine("Back to previous menu input 'back'\n");
        printer.printLine("Exit from program input 'exit'\n");
    }

    private void printUpdateProductStatusMenu() {
        printer.printLine("Update product status menu\n");
        printer.printLine(" - to change product status on 'In stock' press '1'\n");
        printer.printLine(" - to change product status on 'Running low' press '2'\n");
        printer.printLine(" - to change product status on 'Out of stock' press '2'\n");
        printer.printLine("Back to previous menu input 'back'\n");
        printer.printLine("Exit from program input 'exit'\n");
    }
}
