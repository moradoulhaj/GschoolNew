package com.gschool.controller.web;

import com.gschool.entities.User;
import com.gschool.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

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

            // Send Telegram notification after successful login
            sendTelegramNotification(user.getLogin());

            return ResponseEntity.ok(Map.of("message", "Login successful"));
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid login or password"));
        }
    }

    // Method to send Telegram notification
    private void sendTelegramNotification(String Login) {
        String botToken = "7539316932:AAGh-8kkGwF8dVS34il7guUdF0hwM1OlDVI"; // Replace with your bot token
        String chatId = "7365045675"; // Replace with your chat ID
        String message = "User " + Login + " has logged in.";

        String telegramApiUrl = "https://api.telegram.org/bot" + botToken + "/sendMessage";
        String url = telegramApiUrl + "?chat_id=" + chatId + "&text=" + message;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        restTemplate.postForObject(url, entity, String.class);
    }

    // Logout endpoint to invalidate session
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // Invalidate the session
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }
}
