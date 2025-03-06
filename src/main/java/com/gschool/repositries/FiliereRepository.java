package com.gschool.repositries;

import com.gschool.entities.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FiliereRepository extends JpaRepository <Filiere , Integer> {
    boolean existsByCode (String code);
}
