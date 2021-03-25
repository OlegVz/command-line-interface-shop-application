package com.hybris.shop.controllers;

import com.hybris.shop.dto.UserDto;
import com.hybris.shop.facade.impl.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserFacade userFacade;

    public UserController() {
    }

    @Autowired
    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @GetMapping("/list")
    public String listUsers(Model model) {

        List<UserDto> allUsers = userFacade.findAll();

        model.addAttribute("users", allUsers);

        return "userList";
    }

    @GetMapping("/user-by-id")
    public String getUserById(HttpServletRequest request, Model model) {
//        long id = Long.parseLong(request.getParameter("id"));
        Long id = 5L;
        UserDto userById = userFacade.findById(id);
        model.addAttribute("user", userById.toString());

        return "views/user/getById";
    }
}
