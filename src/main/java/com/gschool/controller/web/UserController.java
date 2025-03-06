package com.gschool.controller.web;

import com.gschool.entities.Student;
import com.gschool.entities.User;
import com.gschool.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody User user) {
        // Check if a student with the same CNE exists
        if (userService.existsByLogin(user.getLogin())){
            // Return conflict status with an error message in JSON format
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User with the same Login already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }


        User newUser = userService.addUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @Valid @RequestBody User userDetails) {
        // Fetch the existing student by ID
        User existingUser = userService.getUserById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build(); // Student not found
        }

        // Check if the Login already exists for another student
        if (!existingUser.getLogin().equals(userDetails.getLogin()) &&
                userService.existsByLogin(userDetails.getLogin())) {
            // Return an error message wrapped in ResponseEntity
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Login already exists.");
            return ResponseEntity.badRequest().body(errorResponse);
        }


        User updatedUser = userService.updateUser(id, userDetails);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalUsers() {
        Long totalUsers = userService.getTotalUsers();
        return ResponseEntity.ok(totalUsers);
    }
}