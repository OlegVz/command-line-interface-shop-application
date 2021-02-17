package com.hybris.shop.view.menu.userMenu;

import com.hybris.shop.ShopApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ShopApplication.class})
class UserMenuTestIT {

    @Autowired
    private UserMenu userMenu;

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void shouldPrintUserMenuAndExit() {
        //given
        //when
        String simulatedUserInput = "exit" + System.getProperty("line.separator");
        InputStream savedStandardInputStream = System.in;
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        //when
        userMenu.menu();

        System.setIn(savedStandardInputStream);

        //then
        assertEquals("User menu\n" +
                "\t- to list user orders history press '1';\n" +
                "\t- to update user press '2';\n" +
                "\t- to log out press '3';\n" +
                "\t- to delete current user press '4';\n" +
                "\t- to delete user by id press '5';\n" +
                "\t- to list all users '6';\n" +
                "\t- to list all user orders '7';\n" +
                "Back to previous menu input 'back'\n" +
                "Exit from program input 'exit'\n" +
                ">> ", outputStreamCaptor.toString());
    }

    @Test
    void shouldPrintUserMenuAndBack() {
        //given
        //when
        String simulatedUserInput = "back" + System.getProperty("line.separator");
        InputStream savedStandardInputStream = System.in;
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        //when
        userMenu.menu();

        System.setIn(savedStandardInputStream);

        //then
        assertEquals("User menu\n" +
                "\t- to list user orders history press '1';\n" +
                "\t- to update user press '2';\n" +
                "\t- to log out press '3';\n" +
                "\t- to delete current user press '4';\n" +
                "\t- to delete user by id press '5';\n" +
                "\t- to list all users '6';\n" +
                "\t- to list all user orders '7';\n" +
                "Back to previous menu input 'back'\n" +
                "Exit from program input 'exit'\n" +
                ">> ", outputStreamCaptor.toString());
    }
}