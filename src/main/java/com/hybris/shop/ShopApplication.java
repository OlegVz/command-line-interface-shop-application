package com.hybris.shop;

import com.hybris.shop.view.menu.Menu;
import com.hybris.shop.view.menu.MenuInterface;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:local.properties"}, ignoreResourceNotFound = true)
@ImportResource("classpath:shop-config.xml")
public class ShopApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ShopApplication.class, args);

        MenuInterface menuInterface = context.getBean(Menu.class);
        menuInterface.menu();

        context.close();
    }
}
