package com.gschool.controller.ui;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiLoginController {

    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        // Check if the user is already logged in by checking session
        if (session.getAttribute("user") != null) {
            // Redirect to dashboard if the user is authenticated
            return "redirect:/dashboard";  // Or any other page
        }
        return "login";  // Return the login page if not authenticated
    }
}
