package com.gschool.controller.web;

import com.gschool.entities.Filiere;
import com.gschool.service.FiliereService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/filieres")
public class FiliereController {
    private final FiliereService filiereService;

    public FiliereController(FiliereService filiereService) {
        this.filiereService = filiereService;
    }

    @GetMapping
    public ResponseEntity<List<Filiere>> getAllFilieres() {
        List<Filiere> filieres = filiereService.getAllFilieres();
        return new ResponseEntity<>(filieres, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Filiere> getFiliereById(@PathVariable Integer id) {
        Filiere filiere = filiereService.getFiliereById(id);
        return filiere != null ? ResponseEntity.ok(filiere) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Filiere> addFiliere(@Valid @RequestBody Filiere filiere) {
        Filiere newFiliere = filiereService.addFiliere(filiere);
        return new ResponseEntity<>(newFiliere, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Filiere> updateFiliere(@PathVariable Integer id, @Valid @RequestBody Filiere filiereDetails) {
        Filiere updatedFiliere = filiereService.updateFiliere(id, filiereDetails);
        return updatedFiliere != null ? ResponseEntity.ok(updatedFiliere) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFiliere(@PathVariable Integer id) {
        filiereService.deleteFiliere(id);
        return ResponseEntity.noContent().build();
    }
}