package com.hybris.shop;

import com.hybris.shop.config.ShopApplicationConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:local.properties"}, ignoreResourceNotFound = true)
public class ShopApplication {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ShopApplicationConfig.class);

        context.close();
    }

}
