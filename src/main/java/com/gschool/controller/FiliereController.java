package com.gschool.controller;

import com.gschool.entities.Filiere;
import com.gschool.service.FiliereService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filieres")
public class FiliereController {
    private final FiliereService filiereService;

    public FiliereController(FiliereService filiereService) {
        this.filiereService = filiereService;
    }

    @GetMapping
    public List<Filiere> getAllFilieres() {
        return filiereService.getAllFilieres();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Filiere> getFiliereById(@PathVariable Integer id) {
        Filiere filiere = filiereService.getFiliereById(id);
        return filiere != null ? ResponseEntity.ok(filiere) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Filiere addFiliere(@RequestBody Filiere filiere) {
        return filiereService.addFiliere(filiere);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Filiere> updateFiliere(@PathVariable Integer id, @RequestBody Filiere filiereDetails) {
        Filiere updatedFiliere = filiereService.updateFiliere(id, filiereDetails);
        return updatedFiliere != null ? ResponseEntity.ok(updatedFiliere) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFiliere(@PathVariable Integer id) {
        filiereService.deleteFiliere(id);
        return ResponseEntity.noContent().build();
    }
}
