package com.example.TPINF232.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TPINF232.dto.formulaire.FormulaireRequest;
import com.example.TPINF232.dto.formulaire.FormulaireResponse;
import com.example.TPINF232.model.Formulaire;
import com.example.TPINF232.service.FormulaireService;

import lombok.Data;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@Data
@RequestMapping("/api/formulaires")

public class FormulaireController {
    
    @Autowired
    private FormulaireService formulaireService;

    // Creation d'un formulaire
    @PostMapping("/create")
    public ResponseEntity<Formulaire> createForm(@RequestBody FormulaireRequest formulaireRequest) {
        
        try {
         
            Formulaire createdForm = formulaireService.createForm(formulaireRequest);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(createdForm);
        
        } catch (Exception e) {
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Mise à jour d'un formulaire
    @PutMapping("/update/{id}")
    public ResponseEntity<Formulaire> updateForm(@PathVariable int id, @RequestBody FormulaireRequest formulaireRequest) {
        
        try {
         
            Formulaire updatedForm = formulaireService.updateForm(id, formulaireRequest);
            
            if (updatedForm != null) {
                return ResponseEntity.ok(updatedForm);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        
        } catch (Exception e) {
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Récupérer un formulaire par son ID
    @GetMapping("/get/{id}")
    public ResponseEntity<FormulaireResponse> getForm(@PathVariable int id) {
        try {
            FormulaireResponse form = formulaireService.getFormByIdAsResponse(id);
            if (form != null) return ResponseEntity.ok(form);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    

    // Récupérer tous les formulaires
    @GetMapping("/all")
    public ResponseEntity<List<FormulaireResponse>> getAllForms() {
        
        try {
        
            return ResponseEntity.ok(formulaireService.getAllFormsAsResponse());
        
        } catch (Exception e) {
        
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Récupérer les formulaires d'un utilisateur
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Formulaire>> getFormsByUser(@PathVariable int userId) {
     
        try {
            
            List<Formulaire> formulaires = formulaireService.getFormsByUser(userId);
            
            return ResponseEntity.ok(formulaires);
        
        } catch (Exception e) {
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Suppression d'un formulaire
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteForm(@PathVariable int id) {
        try {

            formulaireService.deleteForm(id);

            return ResponseEntity.noContent().build();

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
