package com.hybris.shop.view.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Input {
    public static String command;

    private final Printer<String> printer;

    @Autowired
    public Input(Printer<String> printer) {
        this.printer = printer;
    }

    public String getCommand() {
        printer.printLine(">> ");
        return new Scanner(System.in).nextLine();
    }
}
