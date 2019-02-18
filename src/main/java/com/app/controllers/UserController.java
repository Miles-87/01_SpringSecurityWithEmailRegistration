package com.app.controllers;

import com.app.dto.security.UserDto;
import com.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
    {
        "username": "u",
        "email": "u@gmail.com",
        "password": "1234",
        "passwordConfirmation": "1234",
        "role": "USER"
    }
    */
    @GetMapping("/new")
    public String registerNewUser(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "security/register";
    }

    @PostMapping("/new")
    public String registerNewUserPOST(@ModelAttribute UserDto userDto, HttpServletRequest request) {
        userService.registerNewUser(userDto, request);
        return "redirect:/";
    }

    @GetMapping("/registrationConfirm")
    public String token(@RequestParam String token) {
        userService.activateUser(token);
        return "redirect:/";
    }
}
