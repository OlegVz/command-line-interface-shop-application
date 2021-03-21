package com.hybris.shop.view.consoleInputOutput.impl;

import com.hybris.shop.view.consoleInputOutput.InputInterface;
import com.hybris.shop.view.consoleInputOutput.PrinterInterface;

import java.util.Scanner;

//@Component
public class Input implements InputInterface {
    public static String command;

    private final PrinterInterface<String> printerInterface;

//    @Autowired
    public Input(PrinterInterface<String> printerInterface) {
        this.printerInterface = printerInterface;
    }

    @Override
    public String getCommand() {
        printerInterface.printLine(">> ");
        return new Scanner(System.in).nextLine();
    }
}
