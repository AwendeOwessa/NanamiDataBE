package com.example.TPINF232.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.TPINF232.dto.reponse.ReponseRequest;
import com.example.TPINF232.model.Reponse;
import com.example.TPINF232.service.ReponseService;

import lombok.Data;

@RestController
@Data
@RequestMapping("/api/reponses")

public class ReponseControler {

    @Autowired
    private ReponseService reponseService;

    // Soumission d'une réponse à un formulaire
    @PostMapping("/submit")

    public ResponseEntity<Reponse> submitReponse(@RequestBody ReponseRequest reponseRequest) {
    
        try {
    
            Reponse reponse = reponseService.submitReponse(reponseRequest);
    
            return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
    
        } catch (Exception e) {
    
            System.err.println("Erreur submit : " + e.getMessage());
    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Récupération de toutes les réponses pour un formulaire donné
    @GetMapping("/formulaire/{formId}")
    public ResponseEntity<List<Reponse>> getResponsesByForm(@PathVariable int formId) {
    
        try {
    
            return ResponseEntity.ok(reponseService.getResponsesByForm(formId));
    
        } catch (Exception e) {
    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Récupération d'une réponse par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Reponse> getResponseById(@PathVariable int id) {
    
        try {
    
            Reponse reponse = reponseService.getResponseById(id);
    
            if (reponse != null) {
                return ResponseEntity.ok(reponse);
            }
    
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    
        } catch (Exception e) {
    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Suppression d'une réponse par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable int id) {
    
        try {
    
            boolean deleted = reponseService.deleteResponse(id);
    
            if (deleted) {
                return ResponseEntity.noContent().build();
            }
    
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    
        } catch (Exception e) {
    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Comptage du nombre de réponses pour un formulaire donné
    @GetMapping("/count/{formId}")
    public ResponseEntity<Long> countResponsesByForm(@PathVariable int formId) {
    
        try {
    
            return ResponseEntity.ok(reponseService.countResponsesByForm(formId));
    
        } catch (Exception e) {
    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}