package com.hybris.shop;

import com.hybris.shop.view.menu.MainMenu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:local.properties"}, ignoreResourceNotFound = true)
public class ShopApplication {

    public static void main(String[] args) {
    SpringApplication.run(ShopApplication.class, args);
//        ConfigurableApplicationContext context = SpringApplication.run(ShopApplication.class, args);
//
//        MainMenu mainMenu = context.getBean(MainMenu.class);
//        mainMenu.menu();
//
//        context.close();
    }
}
