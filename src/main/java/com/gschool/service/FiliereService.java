package com.gschool.service;

import com.gschool.entities.Filiere;
import com.gschool.repositries.FiliereRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FiliereService {
    private final FiliereRepository filiereRepository;

    public FiliereService(FiliereRepository filiereRepository) {
        this.filiereRepository = filiereRepository;
    }

    public List<Filiere> getAllFilieres() {
        return filiereRepository.findAll();
    }

    public Filiere getFiliereById(Integer id) {
        return filiereRepository.findById(id).orElse(null);
    }

    public Filiere addFiliere(Filiere filiere) {
        return filiereRepository.save(filiere);
    }

    public Filiere updateFiliere(Integer id, Filiere filiereDetails) {
        Optional<Filiere> optionalFiliere = filiereRepository.findById(id);
        if (optionalFiliere.isPresent()) {
            Filiere filiere = optionalFiliere.get();
            filiere.setCode(filiereDetails.getCode());
            filiere.setLibelle(filiereDetails.getLibelle());
            return filiereRepository.save(filiere);
        }
        return null;
    }

    public void deleteFiliere(Integer id) {
        filiereRepository.deleteById(id);
    }
}
