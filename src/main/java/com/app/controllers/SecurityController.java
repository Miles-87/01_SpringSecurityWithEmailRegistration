package com.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("error", "");
        return "security/loginForm";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("error", "Nieprawidłowe dane logowania");
        return "security/loginForm";
    }

    @GetMapping("/accessDenied")
    public String accessDenied(Model model) {
        model.addAttribute("message", "Nie masz uprawnien do przegladania tej strony");
        return "security/forbidden";
    }
}
