package com.gschool.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiLoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // Refers to the 'login.html' template
    }
}
