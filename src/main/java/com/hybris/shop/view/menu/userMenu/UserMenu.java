package com.hybris.shop.view.menu.userMenu;

import com.hybris.shop.dto.NewUserDto;
import com.hybris.shop.dto.UserDto;
import com.hybris.shop.dto.UserOrdersDto;
import com.hybris.shop.exceptions.userExceptions.InvalidLoginOrPasswordException;
import com.hybris.shop.exceptions.userExceptions.UserNotFoundByIdException;
import com.hybris.shop.facade.impl.UserFacade;
import com.hybris.shop.util.EmailValidatorUtil;
import com.hybris.shop.util.PasswordValidatorUtil;
import com.hybris.shop.view.consoleInputOutput.Input;
import com.hybris.shop.view.consoleInputOutput.Printer;
import com.hybris.shop.view.menu.commands.Commands;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.hybris.shop.view.consoleInputOutput.Input.command;
import static com.hybris.shop.view.menu.commands.CommandsValidator.*;

//@Component
@SuppressWarnings({"unchecked", "rawtypes"})
public class UserMenu {

    private static final String SUCCESS_COMMAND = Commands.SUCCESS.getCommand();

    private final Printer printer;
    private final Input input;

    private final PasswordValidatorUtil passwordValidatorUtil;
    private final EmailValidatorUtil emailValidatorUtil;

    private final UserFacade userFacade;

    public static Long currentUserId;

    //    @Autowired
    public UserMenu(UserFacade userFacade,
                    Printer printer,
                    Input input,
                    EmailValidatorUtil emailValidatorUtil,
                    PasswordValidatorUtil passwordValidatorUtil/*,
                    @Lazy MainMenu mainMenu*/) {
        this.userFacade = userFacade;
        this.printer = printer;
        this.input = input;
        this.emailValidatorUtil = emailValidatorUtil;
        this.passwordValidatorUtil = passwordValidatorUtil;
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
                    printListOfUserOrders();
                    break;
                case "2":
                    updateCurrentUserData();
                    break;
                case "3":
                    logOut();
                    if (isLogOutCommand(command)) {
                        return;
                    }
                    break;
                case "4":
                    deleteCurrentUser();
                    if (isLogOutCommand(command)) {
                        return;
                    }
                    break;
                case "5":
                    deleteUserById();
                    break;
                case "6":
                    listAllUsers();
                    break;
                default:
                    printer.printLine("Invalid command: " + command + "\n");
            }

            if (isExitCommand(command)) {
                return;
            }
        } while (true);
    }

    private void listAllUsers() {
        printer.printLine("All users\n");
        List<UserDto> allUsers = userFacade.findAll();
        printer.printTable(allUsers);
    }

    private void deleteUserById() {
        printer.printLine("Delete user by ID\n");
        printer.printLine("User list\n");
        List<UserDto> allUsers = userFacade.findAll();

        printer.printTable(allUsers);

        Long userId = setUserId();

        if (isExitCommand(command) || isBAckCommand(command)) {
            return;
        }

        printer.printLine("User to remove:\n");
        UserDto userById = userFacade.findById(userId);
        printer.printTable(List.of(userById));

        if (confirmCommand("Remove user?")) {
            userFacade.deleteById(userId);
            printer.printLine("User removed\n");
        }
    }

    private Long setUserId() {
        Long userId = null;
        boolean isUserExist = false;

        do {
            try {
                printer.printLine("Select user id\n");
                command = input.getCommand();
                if (isExitCommand(command) || isBAckCommand(command)) {
                    break;
                }

                userId = Long.parseLong(command);

                isUserExist = userFacade.existsById(userId);
                if (!isUserExist) {
                    throw new UserNotFoundByIdException(userId);
                }
            } catch (NumberFormatException ex) {
                printer.printLine(String.format("Invalid input '%s'. Please input only numbers!\n", command));
            } catch (UserNotFoundByIdException ex) {
                printer.printLine(String.format("User with id '%s' not fount. Please input id from product table!\n",
                        command));
            }
        } while (!isUserExist);

        return userId;
    }

    private void updateCurrentUserData() {
        NewUserDto newUserDto = new NewUserDto();

        do {
            printUpdateCurrentMenu();

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
                        printer.printLine("Input new password\n");

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
                        printer.printLine("New user data\n");
                        printer.printTable(List.of(update));

                        return;
                    }
                    break;
                default:
                    printer.printLine("Invalid command: " + command + "\n");
            }
        } while (true);
    }

    public boolean isPasswordCorrect(String password) {
        return userFacade.chekPassword(currentUserId, password);
    }

    private void printUpdateCurrentMenu() {
        printer.printLine("Update current user data menu\n");
        printer.printLine(" - press '1' to update user email;\n");
        printer.printLine(" - press '2' to update password\n");
        printer.printLine(" - press '3' to save changes\n");
        printer.printLine("Back to previous menu input 'back'\n");
        printer.printLine("Exit from program input 'exit'\n");
    }

    private void deleteCurrentUser() {
        if (confirmCommand("Confirm delete user?")) {
            userFacade.deleteById(currentUserId);
            command = Commands.LOGOUT.getCommand();
        }
    }

    private void logOut() {
        if (confirmCommand("Log out?")) {
            command = Commands.LOGOUT.getCommand();
        }
    }

    public void userRegistration() {
        NewUserDto newUserDto = new NewUserDto();

        boolean b;

        do {
            validateAndSetEmail("Input user email", newUserDto);
            if (isExitCommand(command) || isBAckCommand(command)) {
                return;
            }

            String userEmail = newUserDto.getEmail();

            b = userFacade.existsByEmail(userEmail);

            if (b) {
                printer.printLine(String.format("User with such email '%s' exist\n", userEmail));
            }
        } while (b);

        validateAndSetPassword("Input password", newUserDto);
        if (isExitCommand(command) || isBAckCommand(command)) {
            return;
        }

        UserDto savedUser = userFacade.save(newUserDto);
        currentUserId = savedUser.getId();

        printer.printLine("Saved user:\n");
        printer.printTable(List.of(savedUser));

        command = SUCCESS_COMMAND;
    }

    private void validateAndSetEmail(String msg, NewUserDto newUserDto) {
        do {
            printer.printLine(String.format("%s\n", msg));

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
            printer.printLine("Input login\n");

            command = input.getCommand();
            if (isExitCommand(command) || isBAckCommand(command)) {
                return;
            }

            newUserDto.setEmail(command);

            printer.printLine("Input password\n");

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

    public void printListOfUserOrders() {
        List<UserOrdersDto> allUserOrders = userFacade.findAllUserOrders(currentUserId).stream()
                .sorted(Comparator.comparingLong(UserOrdersDto::getId))
                .collect(Collectors.toList());

        if (allUserOrders.size() != 0) {
            printer.printLine("Orders list\n");
            printer.printTable(allUserOrders);
        } else {
            printer.printLine("Order list is empty!\n");
            command = BACK_COMMAND;
        }
    }

    private void printUserMenu() {
        printer.printLine("User menu\n");
        printer.printLine("\t- to list user orders history press '1';\n");
        printer.printLine("\t- to update user press '2';\n");
        printer.printLine("\t- to log out press '3';\n");
        printer.printLine("\t- to delete current user press '4';\n");
        printer.printLine("\t- to delete user by id press '5';\n");
        printer.printLine("\t- to list all users '6';\n");
        printer.printLine("\t- to list all user orders '7';\n");
        printer.printLine("Back to previous menu input 'back'\n");
        printer.printLine("Exit from program input 'exit'\n");
    }
}
