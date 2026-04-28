package com.example.TPINF232.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TPINF232.dto.Utilisateur.utilisateur.UtilisateurLogin;
import com.example.TPINF232.dto.Utilisateur.utilisateur.UtilisateurRequest;
import com.example.TPINF232.dto.Utilisateur.utilisateur.UtilisateurResponse;

import com.example.TPINF232.service.UtilisateurService;

import lombok.Data;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@Data
@RequestMapping("/api/utilisateurs")

public class UtilisateurController {
    
    @Autowired
    private UtilisateurService utilisateurService;

    //Enregistrement d'un utilisateur
    @PostMapping("/register")
    public ResponseEntity<?> registerUtilisateur(@RequestBody UtilisateurRequest utilisateurRequest) {
        
        try {
        
            UtilisateurResponse utilisateurResponse = utilisateurService.registerUtilisateur(utilisateurRequest);
        
            return ResponseEntity.status(HttpStatus.CREATED).body(utilisateurResponse);

        } catch (Exception e) {
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'enregistrement de l'utilisateur : " + e.getMessage());
        }
    }

    //Connexion d'un utilisateur
    @PostMapping("/login")
    public ResponseEntity<?> loginUtilisateur(@RequestBody UtilisateurLogin utilisateurLogin) {
        
        try {
        
            UtilisateurResponse utilisateurResponse = utilisateurService.loginUtilisateur(utilisateurLogin);
        
            if (utilisateurResponse != null) {
                return ResponseEntity.ok(utilisateurResponse);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou mot de passe incorrect");
            }

        } catch (Exception e) {
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la connexion de l'utilisateur : " + e.getMessage());
        }
    }

    //Trouver un utilisateur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurResponse> findUserById(@PathVariable int id) {

        UtilisateurResponse utilisateur = utilisateurService.findUserById(id);

        if (utilisateur != null) {
            return ResponseEntity.ok(utilisateur);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //Trouver un utilisateur par son email
    @GetMapping("/email")
    public ResponseEntity<UtilisateurResponse> findUserByEmail(@RequestParam String email) {

        UtilisateurResponse utilisateur = utilisateurService.findUserByEmail(email);

        if (utilisateur != null) {
            return ResponseEntity.ok(utilisateur);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    //Récupérer tous les utilisateurs
    @GetMapping("/all")
    public ResponseEntity<List<UtilisateurResponse>> getAllUsers() {
        
        List<UtilisateurResponse> utilisateurs = utilisateurService.getAllUsers();
        
        return ResponseEntity.ok(utilisateurs);
    }
    //Supprimer un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        
        boolean deleted = utilisateurService.deleteUser(id);
        
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
