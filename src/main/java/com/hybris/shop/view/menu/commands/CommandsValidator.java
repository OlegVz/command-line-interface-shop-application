package com.hybris.shop.view.menu.commands;

import com.hybris.shop.view.consoleInputOutput.InputInterface;
import com.hybris.shop.view.consoleInputOutput.PrinterInterface;

import static com.hybris.shop.view.consoleInputOutput.impl.Input.command;

//@Component
public class CommandsValidator {
    private static final String EXIT_COMMAND = Commands.EXIT.getCommand();
    private static final String BACK_COMMAND = Commands.BACK.getCommand();
    private static final String SUCCESS_COMMAND = Commands.SUCCESS.getCommand();
    private static final String YES_COMMAND = Commands.YES.getCommand();
    private static final String NO_COMMAND = Commands.NO.getCommand();
    private static final String LOGOUT_COMMAND = Commands.LOGOUT.getCommand();

    private static PrinterInterface<String> printerInterface;
    private static InputInterface inputInterface;

//    @Autowired
    public CommandsValidator(PrinterInterface<String> printerInterface, InputInterface inputInterface) {
        CommandsValidator.printerInterface = printerInterface;
        CommandsValidator.inputInterface = inputInterface;
    }

    public static boolean confirmCommand(String message) {
        boolean isYes ;
        boolean isNo;

        do {
            printerInterface.printLine(String.format("%s (y/n):\n", message));
            command = inputInterface.getCommand();
            isYes = YES_COMMAND.equals(command);
            isNo = NO_COMMAND.equals(command);
        } while (!isYes && !isNo);

        return isYes;
    }

    public static boolean isExitCommand(String command) {
        return EXIT_COMMAND.equals(command);
    }

    public static boolean isLogOutCommand(String command) {
        return LOGOUT_COMMAND.equals(command);
    }

    public static boolean isBAckCommand(String command) {
        return BACK_COMMAND.equals(command);
    }

    public static boolean isSuccessCommand(String command) {
        return SUCCESS_COMMAND.equals(command);
    }
}
