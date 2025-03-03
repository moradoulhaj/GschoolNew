package com.gschool.controller;

import com.gschool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Authenticate the user
        boolean isAuthenticated = userService.authenticate(loginRequest.getLogin(), loginRequest.getPassword());
        System.out.println(isAuthenticated);
        if (isAuthenticated) {
            // Login successful
            return ResponseEntity.ok("Login successful");
        } else {
            // Login failed
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid login or password"); // Custom error message
        }
    }
}