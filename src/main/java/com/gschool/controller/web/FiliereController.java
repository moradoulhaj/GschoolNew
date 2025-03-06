package com.gschool.controller.web;

import com.gschool.entities.Filiere;
import com.gschool.service.FiliereService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> addFiliere(@Valid @RequestBody Filiere filiere) {
        // Check if a Filiere with the same code exists
        if (filiereService.existsByCode(filiere.getCode())) {
            // Return conflict status with an error message in JSON format
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Filière with the same code already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        // Save the new Filiere if no duplicate is found
        Filiere newFiliere = filiereService.addFiliere(filiere);
        return new ResponseEntity<>(newFiliere, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFiliere(@PathVariable Integer id, @Valid @RequestBody Filiere filiereDetails) {
        // Fetch the existing filière by ID
        Filiere existingFiliere = filiereService.getFiliereById(id);
        if (existingFiliere == null) {
            return ResponseEntity.notFound().build(); // Filière not found
        }

        // Check if the code already exists for another filière
        if (!existingFiliere.getCode().equals(filiereDetails.getCode()) &&
                filiereService.existsByCode(filiereDetails.getCode())) {
            // Return an error message wrapped in ResponseEntity
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Code already exists.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Update the filière
        Filiere updatedFiliere = filiereService.updateFiliere(id, filiereDetails);
        return ResponseEntity.ok(updatedFiliere);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFiliere(@PathVariable Integer id) {
        filiereService.deleteFiliere(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalFilieres() {
        Long totalFilieres = filiereService.getTotalFilieres();
        return ResponseEntity.ok(totalFilieres);

    }
}