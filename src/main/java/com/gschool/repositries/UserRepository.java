package com.gschool.repositries;

import com.gschool.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Integer> {
    // Find a user by login
    Optional<User> findByLogin(String login);
    boolean existsByLogin(String login);
}
