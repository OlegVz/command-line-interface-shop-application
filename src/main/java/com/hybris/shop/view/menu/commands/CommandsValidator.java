package com.hybris.shop.view.menu.commands;

import com.hybris.shop.view.console.Input;
import com.hybris.shop.view.console.Printer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.hybris.shop.view.console.Input.command;

@Component
public class CommandsValidator {
    private static final String EXIT_COMMAND = Commands.EXIT.getCommand();
    public static final String BACK_COMMAND = Commands.BACK.getCommand();
    private static final String SUCCESS_COMMAND = Commands.SUCCESS.getCommand();
    private static final String YES_COMMAND = Commands.YES.getCommand();
    private static final String NO_COMMAND = Commands.NO.getCommand();

    private static Printer<String> printer;
    private static Input input;

    @Autowired
    public CommandsValidator(Printer<String> printer, Input input) {
        CommandsValidator.printer = printer;
        CommandsValidator.input = input;
    }

    public static boolean confirmCommand(String message) {
        boolean isYes ;
        boolean isNo;

        do {
            printer.printLine(String.format("%s (y/n):\n", message));
            command = input.getCommand();
            isYes = YES_COMMAND.equals(command);
            isNo = NO_COMMAND.equals(command);
        } while (!isYes && !isNo);

        return isYes;
    }

    public static boolean isExitCommand(String command) {
        return EXIT_COMMAND.equals(command);
    }

    public static boolean isBAckCommand(String command) {
        return BACK_COMMAND.equals(command);
    }

    public static boolean isSuccessCommand(String command) {
        return SUCCESS_COMMAND.equals(command);
    }
}
