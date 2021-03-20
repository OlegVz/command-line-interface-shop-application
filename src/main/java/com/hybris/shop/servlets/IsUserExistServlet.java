package com.hybris.shop.servlets;

import com.hybris.shop.facade.impl.UserFacade;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class IsUserExistServlet extends HttpServlet {

    private UserFacade userFacade;

    public IsUserExistServlet() {
    }

    @Autowired
    public IsUserExistServlet(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        boolean b = userFacade.existsByEmail(email);

        PrintWriter pw = response.getWriter();
        pw.println("<html>");
        pw.println("<h1> Is user with email exist: " + b + "</h1>");
        pw.println("<h1> Is user with email exist: </h1>");
        pw.println("</html>");
    }
}
