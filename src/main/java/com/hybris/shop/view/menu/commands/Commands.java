package com.hybris.shop.view.menu.commands;

public enum Commands {
    EXIT("exit"),
    BACK("back"),
    SUCCESS("success"),
    YES("y"),
    NO("n"),
    LOGOUT("logout");

    private final String command;

    Commands(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
