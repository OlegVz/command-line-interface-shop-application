package com.hybris.shop.view.menu.userMenu;

import com.hybris.shop.dto.NewUserDto;
import com.hybris.shop.dto.UserDto;
import com.hybris.shop.facade.impl.UserFacade;
import com.hybris.shop.util.EmailValidatorUtil;
import com.hybris.shop.util.PasswordValidatorUtil;
import com.hybris.shop.view.console.Input;
import com.hybris.shop.view.console.Printer;
import com.hybris.shop.view.menu.commands.Commands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.hybris.shop.view.console.Input.command;

@Component
public class UserMenu {

    private static final String SUCCESS_COMMAND = Commands.SUCCESS.getCommand();
    private static final String EXIT_COMMAND = Commands.EXIT.getCommand();
    private static final String BACK_COMMAND = Commands.BACK.getCommand();

    private final UserFacade userFacade;
    private final Printer<UserDto> printer;
    private final Input input;
    private final PasswordValidatorUtil passwordValidatorUtil;
    private final EmailValidatorUtil emailValidatorUtil;

    private static Long currentUserId;

    @Autowired
    public UserMenu(UserFacade userFacade,
                    Printer<UserDto> printer,
                    Input input,
                    EmailValidatorUtil emailValidatorUtil,
                    PasswordValidatorUtil passwordValidatorUtil) {
        this.userFacade = userFacade;
        this.printer = printer;
        this.input = input;
        this.emailValidatorUtil = emailValidatorUtil;
        this.passwordValidatorUtil = passwordValidatorUtil;
    }

    public void menu() {
        do {
            command = input.getCommand();
            if (EXIT_COMMAND.equals(command) || BACK_COMMAND.equals(command)) {
                break;
            }
        } while (!"exit".equals(command));
    }

    public void userRegistration() {
        NewUserDto newUserDto = new NewUserDto();

        do {
            printer.printLine("Input user email");

            command = input.getCommand();
            if (EXIT_COMMAND.equals(command) || BACK_COMMAND.equals(command)) {
                return;
            }

            if (emailValidatorUtil.validateEmail(command)) {
                newUserDto.setEmail(command);
                break;
            } else {
                printer.printLine("Invalid email\n");
            }
        } while (true);

        do {
            printer.printLine("Input password");

            command = input.getCommand();
            if (EXIT_COMMAND.equals(command) || BACK_COMMAND.equals(command)) {
                return;
            }

            if (passwordValidatorUtil.validatePassword(command)) {
                newUserDto.setPassword(command);
                break;
            } else {
                printer.printLine("Invalid password\n");
            }
        } while (true);

        UserDto savedUser = userFacade.save(newUserDto);

        currentUserId = savedUser.getId();

        printer.printLine("Saved user:\n");
        printer.printTable(List.of(savedUser));

        command = SUCCESS_COMMAND;
    }

    private void printUserMenu() {
        printer.printLine("User menu\n");
        printer.printLine("\t- save new user press '1';\n");

    }
}
