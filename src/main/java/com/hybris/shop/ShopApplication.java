package com.hybris.shop;

import com.hybris.shop.dto.NewOrderDto;
import com.hybris.shop.dto.OrderDto;
import com.hybris.shop.facade.impl.OrderFacade;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:local.properties"}, ignoreResourceNotFound = true)
public class ShopApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ShopApplication.class, args);

        OrderFacade bean = context.getBean(OrderFacade.class);

        NewOrderDto newOrderDto = new NewOrderDto();
        
        newOrderDto.setUserId(1L);

        OrderDto save = bean.save(newOrderDto);

        context.close();
    }
}
