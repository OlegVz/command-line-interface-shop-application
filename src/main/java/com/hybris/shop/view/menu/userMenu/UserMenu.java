package com.hybris.shop.view.menu.userMenu;

import com.hybris.shop.dto.NewUserDto;
import com.hybris.shop.dto.UserDto;
import com.hybris.shop.dto.UserOrdersDto;
import com.hybris.shop.exceptions.userExceptions.InvalidLoginOrPasswordException;
import com.hybris.shop.facade.impl.UserFacade;
import com.hybris.shop.util.EmailValidatorUtil;
import com.hybris.shop.util.PasswordValidatorUtil;
import com.hybris.shop.view.console.Input;
import com.hybris.shop.view.console.Printer;
import com.hybris.shop.view.menu.MainMenu;
import com.hybris.shop.view.menu.commands.Commands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.hybris.shop.view.console.Input.command;
import static com.hybris.shop.view.menu.commands.CommandsValidator.*;

@Component
public class UserMenu {

    private static final String SUCCESS_COMMAND = Commands.SUCCESS.getCommand();

    private final UserFacade userFacade;
    private final Printer printer;
    private final Input input;
    private final PasswordValidatorUtil passwordValidatorUtil;
    private final MainMenu mainMenu;
    private final EmailValidatorUtil emailValidatorUtil;

    public static Long currentUserId;

    @Autowired
    public UserMenu(UserFacade userFacade,
                    Printer printer,
                    Input input,
                    EmailValidatorUtil emailValidatorUtil,
                    PasswordValidatorUtil passwordValidatorUtil,
                    @Lazy MainMenu mainMenu) {
        this.userFacade = userFacade;
        this.printer = printer;
        this.input = input;
        this.emailValidatorUtil = emailValidatorUtil;
        this.passwordValidatorUtil = passwordValidatorUtil;
        this.mainMenu = mainMenu;
    }

    public void menu() {
        do {
            printUserMenu();

            command = input.getCommand();
            if (isExitCommand(command) || isBAckCommand(command)) {
                return;
            }

            switch (command) {
                case "1":
                    listUserOrders();
                    break;
                case "2":
                    updateUserData();
                    break;
                case "3":
                    if (confirmCommand("Log out?")) {
                        logOut();
                    }
                    break;
                case "4":
                    if (confirmCommand("Confirm delete user?")) {
                        deleteCurrentUser();
                    }
                    break;
                default:
                    printer.printLine("Invalid command: " + command + "\n");
            }

            if (isExitCommand(command)) {
                return;
            }
        } while (true);
    }

    private void updateUserData() {
        NewUserDto newUserDto = new NewUserDto();

        do {
            printUpdateMenu();

            command = input.getCommand();
            if (isExitCommand(command) || isBAckCommand(command)) {
                return;
            }

            switch (command) {
                case "1":
                    validateAndSetEmail("Input new user email", newUserDto);

                    if (isExitCommand(command) || isBAckCommand(command)) {
                        return;
                    }
                    break;
                case "2":
                    do {
                        printer.printLine("Input password");
                        command = input.getCommand();
                        if (isExitCommand(command) || isBAckCommand(command)) {
                            return;
                        }

                        boolean passwordCorrect = isPasswordCorrect(command);

                        if (!passwordCorrect) {
                            printer.printLine("Invalid password! Please try again\n");
                        } else {
                            break;
                        }
                    } while (true);

                    validateAndSetPassword("Input new password", newUserDto);

                    if (isExitCommand(command) || isBAckCommand(command)) {
                        return;
                    }

                    break;
                case "3":
                    if (confirmCommand("Save changes?")) {
                        UserDto update = userFacade.update(currentUserId, newUserDto);
                        printer.printTable(List.of(update));
                        return;
                    }
                    break;
                default:
                    printer.printLine("Invalid command: " + command + "\n");
            }
        } while (true);

    }

    private boolean isPasswordCorrect(String password) {
        return userFacade.chekPassword(currentUserId, password);
    }

    private void printUpdateMenu() {
        printer.printLine("Update user data menu\n");
        printer.printLine(" - press '1' to update user email;\n");
        printer.printLine(" - press '2' to update password\n");
        printer.printLine(" - press '3' to save changes\n");
        printer.printLine("Back to previous menu input 'back'\n");
        printer.printLine("Exit from program input 'exit'\n");
    }

    private void deleteCurrentUser() {
        userFacade.deleteById(currentUserId);
        logOut();
    }

    private void logOut() {
        currentUserId = null;
        mainMenu.menu();
    }

    public void userRegistration() {
        NewUserDto newUserDto = new NewUserDto();

        validateAndSetEmail("Input user email", newUserDto);

        validateAndSetPassword("Input password", newUserDto);

        UserDto savedUser = userFacade.save(newUserDto);

        currentUserId = savedUser.getId();

        printer.printLine("Saved user:\n");
        printer.printTable(List.of(savedUser));

        command = SUCCESS_COMMAND;
    }

    private void validateAndSetEmail(String msg, NewUserDto newUserDto) {
        do {
            printer.printLine(msg);

            command = input.getCommand();
            if (isExitCommand(command) || isBAckCommand(command)) {
                return;
            }

            if (emailValidatorUtil.validateEmail(command)) {
                newUserDto.setEmail(command);
                break;
            } else {
                printer.printLine("Invalid email\n");
            }
        } while (true);
    }

    private void validateAndSetPassword(String msg, NewUserDto newUserDto) {
        do {
            printer.printLine(msg);

            command = input.getCommand();
            if (isExitCommand(command) || isBAckCommand(command)) {
                return;
            }

            if (passwordValidatorUtil.validatePassword(command)) {
                newUserDto.setPassword(command);
                break;
            } else {
                printer.printLine("Invalid password\n");
            }
        } while (true);
    }

    public void userLogin() {
        NewUserDto newUserDto = new NewUserDto();

        do {
            printer.printLine("Input login");

            command = input.getCommand();
            if (isExitCommand(command) || isBAckCommand(command)) {
                return;
            }
            newUserDto.setEmail(command);

            printer.printLine("Input password");

            command = input.getCommand();
            if (isExitCommand(command) || isBAckCommand(command)) {
                return;
            }

            newUserDto.setPassword(command);


            try {
                currentUserId = userFacade.logIn(newUserDto);
                break;
            } catch (InvalidLoginOrPasswordException ex) {
                printer.printLine("Invalid login or password\n");
            }
        } while (true);

        command = SUCCESS_COMMAND;
    }

    private void listUserOrders() {
        List<UserOrdersDto> allUserOrders = userFacade.findAllUserOrders(currentUserId);

        if (allUserOrders.size() != 0) {
            printer.printLine("Orders history\n");
            printer.printTable(allUserOrders);
        } else {
            printer.printLine("Order list is empty!\n");
        }
    }

    private void printUserMenu() {
        printer.printLine("User menu\n");
        printer.printLine("\t- to list orders history user press '1';\n");
        printer.printLine("\t- to update user press '2';\n");
        printer.printLine("\t- to log out press '3';\n");
        printer.printLine("\t- to delete user press '4';\n");
        printer.printLine("Back to previous menu input 'back'\n");
        printer.printLine("Exit from program input 'exit'\n");
    }
}
