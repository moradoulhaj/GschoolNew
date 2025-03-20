package com.gschool.repositries;

import com.gschool.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Integer> {
    boolean existsByNameAndIdNot(String name, Integer id);
    boolean existsByName(String name);
    List<Session> findByDepId(Integer depId);
}