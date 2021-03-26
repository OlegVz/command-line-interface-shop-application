package com.hybris.shop.servlets.userServlets;

//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;

import com.hybris.shop.dto.NewUserDto;
import com.hybris.shop.facade.impl.UserFacade;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SaveNewUserServlet implements HttpRequestHandler {
    private UserFacade userFacade;

    public SaveNewUserServlet() {
    }

    public SaveNewUserServlet(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setEmail(email);
        newUserDto.setPassword(password);
        userFacade.save(newUserDto);
    }
}

