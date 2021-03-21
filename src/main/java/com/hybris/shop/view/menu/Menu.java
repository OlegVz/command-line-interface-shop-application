package com.hybris.shop.view.menu;

import com.hybris.shop.view.consoleInputOutput.InputInterface;
import com.hybris.shop.view.consoleInputOutput.PrinterInterface;
import com.hybris.shop.view.menu.commands.Commands;
import com.hybris.shop.view.menu.userMenu.UserMenuInterface;

import static com.hybris.shop.view.consoleInputOutput.impl.Input.command;
import static com.hybris.shop.view.menu.commands.CommandsValidator.*;

//@Component
public class Menu implements MenuInterface {

    private final InputInterface inputInterface;
    private final PrinterInterface<String> printerInterface;

    private final UserMenuInterface userMenu;
    private final MenuInterface orderMenu;
    private final MenuInterface productMenu;

//    @Autowired
    public Menu(InputInterface inputInterface,
                PrinterInterface<String> printerInterface,
                UserMenuInterface userMenu,
                MenuInterface orderMenu,
                MenuInterface productMenu) {
        this.inputInterface = inputInterface;
        this.printerInterface = printerInterface;
        this.userMenu = userMenu;
        this.orderMenu = orderMenu;
        this.productMenu = productMenu;
    }

    @Override
    public void menu() {
        helloMenu();

        if (!isExitCommand(command)) {
            mainMenu();
        }

        printerInterface.printLine("Goodbye!\n");
    }

    private void helloMenu() {
        do {
            printHelloMenu();

            command = inputInterface.getCommand();
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
                    printerInterface.printLine("Invalid command: " + command + "\n");
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

            command = inputInterface.getCommand();
            if (isExitCommand(command)) {
                if (confirmCommand("Close program?")) {
                    return;
                }

                mainMenu();
            }

            switch (command) {
                case "1":
                    userMenu.menu();
                    if (isLogOutCommand(command)) {
                        menu();
                    }
                    break;
                case "2":
                    orderMenu.menu();
                    break;
                case "3":
                    productMenu.menu();
                    break;
                default:
                    printerInterface.printLine("Invalid command: " + command + "\n");
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
        printerInterface.printLine("Please log in or create new account\n");
        printerInterface.printLine("\t- to create new account press '1'\n");
        printerInterface.printLine("\t- to log in press '2';\n");
        printerInterface.printLine("Exit from program input 'exit'\n");
    }

    private void printMainMenu() {
        printerInterface.printLine("Please, input your command:\n");
        printerInterface.printLine("\t- to select user menu press '1';\n");
        printerInterface.printLine("\t- to select order menu press '2';\n");
        printerInterface.printLine("\t- to select product menu press '3';\n");
        printerInterface.printLine("Exit from program input 'exit'\n");
    }
}
