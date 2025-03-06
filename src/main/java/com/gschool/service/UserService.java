package com.gschool.service;

import com.gschool.entities.User;
import com.gschool.repositries.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Integer id, User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setNom(userDetails.getNom());
            user.setPrenom(userDetails.getPrenom());
            user.setLogin(userDetails.getLogin());
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
    // Authenticate a user (login)
    public User authenticate(String login, String password) {
        Optional<User> userOptional = userRepository.findByLogin(login);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Compare the provided password with the hashed password in the database
            if(passwordEncoder.matches(password, user.getPassword())){
                return user;
            }
        }

        return null; // User not found
    }
}
