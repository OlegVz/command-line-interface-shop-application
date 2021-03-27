package com.hybris.shop.controllers;

import com.hybris.shop.dto.NewUserDto;
import com.hybris.shop.dto.UserDto;
import com.hybris.shop.facade.impl.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

        return "views/user/userList";
    }

    @GetMapping("/find-user-by-id-form")
    public String findUserByIdForm() {
        return "views/user/findUserByIdForm";
    }

    @GetMapping("/user-by-id")
    public String getUserById(HttpServletRequest request, Model model) {
        long id = Long.parseLong(request.getParameter("id"));
        UserDto userById = userFacade.findById(id);
        model.addAttribute("user", userById);

        return "views/user/getUserById";
    }

    @GetMapping("/delete-user-by-id-form")
    public String deleteUserByIdForm() {
        return "views/user/deleteUserByIdForm";
    }

    @PostMapping("/del-user-by-id")
    public String delUserById(HttpServletRequest request, Model model) {
        long id = Long.parseLong(request.getParameter("id"));
        UserDto userById = userFacade.findById(id);
        userFacade.deleteById(id);
        model.addAttribute("user", userById);

        return "views/user/getUserById";
    }

    @GetMapping("/save-new-user-form")
    public String saveNewUserForm() {
        return "views/user/saveNewUserForm";
    }

    @PostMapping("/save-new-user")
    public String saveNewUser(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setEmail(email);
        newUserDto.setPassword(password);

        UserDto user = userFacade.save(newUserDto);
        model.addAttribute("user", user);

        return "views/user/saveNewUser";
    }

    @GetMapping("update-user-form")
    public String updateUserForm() {
        return "views/user/updateUserForm";
    }

    @PostMapping("update-user")
    public String updateUser(HttpServletRequest request, Model model) {

        Long id = Long.parseLong(request.getParameter("id"));
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        NewUserDto newUserDto = new NewUserDto();
        if (!"".equals(email)) {
            newUserDto.setEmail(email);
        }
        if (!"".equals(password)) {
            newUserDto.setPassword(password);
        }

        UserDto user = userFacade.update(id, newUserDto);
        model.addAttribute("user", user);

        return "views/user/updateUser";
    }
}
