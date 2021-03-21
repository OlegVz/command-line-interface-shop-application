package com.hybris.shop.view.consoleInputOutput;

import java.util.Scanner;

//@Component
public class Input {
    public static String command;

    private final Printer<String> printer;

//    @Autowired
    public Input(Printer<String> printer) {
        this.printer = printer;
    }

    public String getCommand() {
        printer.printLine(">> ");
        return new Scanner(System.in).nextLine();
    }
}
