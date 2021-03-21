package com.hybris.shop.view.menu.userMenu;

import com.hybris.shop.view.menu.MenuInterface;

public interface UserMenuInterface extends MenuInterface {
    boolean isPasswordCorrect(String password);

    void userRegistration();

    void userLogin();

    void printListOfUserOrders();
}
