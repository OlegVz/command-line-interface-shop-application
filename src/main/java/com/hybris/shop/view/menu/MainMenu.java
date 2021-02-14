package com.hybris.shop.view.menu;

import com.hybris.shop.view.console.Input;
import com.hybris.shop.view.console.Printer;
import com.hybris.shop.view.menu.orderMenu.OrderMenu;
import com.hybris.shop.view.menu.userMenu.UserMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.hybris.shop.view.console.Input.command;
import static com.hybris.shop.view.menu.commands.CommandsValidator.isExitCommand;
import static com.hybris.shop.view.menu.commands.CommandsValidator.isSuccessCommand;

@Component
public class MainMenu {

    private final Input input;
    private final Printer<String> printer;

    private final UserMenu userMenu;
    private final OrderMenu orderMenu;

    @Autowired
    public MainMenu(Input input,
                    Printer<String> printer,
                    UserMenu userMenu,
                    OrderMenu orderMenu) {
        this.input = input;
        this.printer = printer;
        this.userMenu = userMenu;
        this.orderMenu = orderMenu;
    }

    public void menu() {
        do {
            printHelloMenu();

            command = input.getCommand();
            if (isExitCommand(command)) {
                return;
            }

            switch (command) {
                case "1":
                    userMenu.userRegistration();
                    if (isExitCommand(command)) {
                        return;
                    }
                    break;
                case "2":
                    userMenu.userLogin();
                    if (isExitCommand(command)) {
                        return;
                    }
                    break;
                default:
                    printer.printLine("Invalid command: " + command + "\n");
            }
        } while (!isSuccessCommand(command));

        do {
            printMainMenu();

            command = input.getCommand();
            if (isExitCommand(command)) {
                return;
            }

            switch (command) {
                case "1":
                    userMenu.menu();
                    break;
                case "2":
                    orderMenu.menu();
                    break;
                default:
                    printer.printLine("Invalid command: " + command + "\n");
            }
        } while (!isExitCommand(command));

        printer.printLine("Goodbye!\n");
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
        printer.printLine("Exit from program input 'exit'\n");
    }
}
