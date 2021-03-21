package com.hybris.shop.servlets;

import com.hybris.shop.facade.impl.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class IsUserExistServlet extends HttpServlet {

    private UserFacade userFacade;

    public IsUserExistServlet() {
    }

    private WebApplicationContext springContext;

    @Autowired
    public IsUserExistServlet(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Override
    public void init(final ServletConfig config) throws ServletException {
            super.init(config);
            SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                    config.getServletContext());
//        super.init(config);
//        springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
//        final AutowireCapableBeanFactory beanFactory = springContext.getAutowireCapableBeanFactory();
//        beanFactory.autowireBean(this);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        boolean b = userFacade.existsByEmail(email);

        PrintWriter pw = response.getWriter();
        pw.println("<html>");
        pw.println("<h1> Is user with email exist: " + b + "</h1>");
        pw.println("</html>");
    }
}
