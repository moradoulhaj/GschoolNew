package com.gschool.controller.web;

import com.gschool.entities.User;
import com.gschool.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        User user = userService.authenticate(loginRequest.getLogin(), loginRequest.getPassword());

        if (user != null) {
            session.setAttribute("user", user); // Store user in session
            return ResponseEntity.ok(Map.of("message", "Login successful"));
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid login or password"));
        }
    }
}
