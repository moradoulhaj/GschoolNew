package com.gschool.controller;

import com.gschool.entities.User;
import com.gschool.service.UserService;
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
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Authenticate the user
        User user = userService.authenticate(loginRequest.getLogin(), loginRequest.getPassword());

        if (user != null) {
            // Create response excluding the password
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("user", Map.of(
                    "id", user.getId(),
                    "nom", user.getNom(),
                    "prenom", user.getPrenom(),
                    "login", user.getLogin()
            ));

            return ResponseEntity.ok(response);
        } else {
            // Login failed
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid login or password"));
        }
    }
}
