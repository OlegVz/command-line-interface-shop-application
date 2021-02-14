package com.hybris.shop.view.menu;

import com.hybris.shop.view.console.Input;
import com.hybris.shop.view.console.Printer;
import com.hybris.shop.view.menu.commands.Commands;
import com.hybris.shop.view.menu.orderMenu.OrderMenu;
import com.hybris.shop.view.menu.productMenu.ProductMenu;
import com.hybris.shop.view.menu.userMenu.UserMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.hybris.shop.view.console.Input.command;
import static com.hybris.shop.view.menu.commands.CommandsValidator.*;

@Component
public class MainMenu {

    private final Input input;
    private final Printer<String> printer;

    private final UserMenu userMenu;
    private final OrderMenu orderMenu;
    private final ProductMenu productMenu;

    @Autowired
    public MainMenu(Input input,
                    Printer<String> printer,
                    UserMenu userMenu,
                    OrderMenu orderMenu,
                    ProductMenu productMenu) {
        this.input = input;
        this.printer = printer;
        this.userMenu = userMenu;
        this.orderMenu = orderMenu;
        this.productMenu = productMenu;
    }

    public void menu() {
        helloMenu();

        if (!isExitCommand(command)) {
            mainMenu();
        }

        printer.printLine("Goodbye!\n");
    }

    private void helloMenu() {
        do {
            printHelloMenu();

            command = input.getCommand();
            if (isExitCommand(command)) {
                if (confirmCommand("Close program?")) {
                    command = Commands.EXIT.getCommand();
                    return;
                }

                helloMenu();
            }

            switch (command) {
                case "1":
                    userMenu.userRegistration();
                    break;
                case "2":
                    userMenu.userLogin();
                    break;
                default:
                    printer.printLine("Invalid command: " + command + "\n");
            }
            if (isExitCommand(command)) {
                if (confirmCommand("Close program?")) {
                    command = Commands.EXIT.getCommand();
                    return;
                }

                helloMenu();
            }
        } while (!isSuccessCommand(command));
    }

    private void mainMenu() {
        do {
            printMainMenu();

            command = input.getCommand();
            if (isExitCommand(command)) {
                if (confirmCommand("Close program?")) {
                    return;
                }

                mainMenu();
            }

            switch (command) {
                case "1":
                    userMenu.menu();
                    break;
                case "2":
                    orderMenu.menu();
                    break;
                case "3":
                    productMenu.menu();
                    break;
                default:
                    printer.printLine("Invalid command: " + command + "\n");
            }

            if (isExitCommand(command)) {
                if (confirmCommand("Close program?")) {
                    command = Commands.EXIT.getCommand();
                    return;
                }

                mainMenu();
            }
        } while (true);
    }

    private void printHelloMenu() {
        printer.printLine("Please log in or create new account\n");
        printer.printLine("\t- to create new account press '1'\n");
        printer.printLine("\t- to log in press '2';\n");
        printer.printLine("Exit from program input 'exit'\n");
    }

    private void printMainMenu() {
        printer.printLine("Please, input your command:\n");
        printer.printLine("\t- to select user menu press '1';\n");
        printer.printLine("\t- to select order menu press '2';\n");
        printer.printLine("\t- to select product menu press '3';\n");
        printer.printLine("Exit from program input 'exit'\n");
    }
}
