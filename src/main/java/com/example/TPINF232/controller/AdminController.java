package com.example.TPINF232.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.TPINF232.model.Formulaire;
import com.example.TPINF232.model.ENUM.Role;
import com.example.TPINF232.dto.Utilisateur.utilisateur.UtilisateurResponse;
import com.example.TPINF232.service.FormulaireService;
import com.example.TPINF232.service.ReponseService;
import com.example.TPINF232.service.UtilisateurService;
import com.example.TPINF232.repository.UtilisateurRepository;
import com.example.TPINF232.model.Utilisateur;

import lombok.Data;

@RestController
@Data
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private FormulaireService formulaireService;

    @Autowired
    private ReponseService reponseService;

    // Tableau de bord global
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        try {
            List<UtilisateurResponse> users    = utilisateurService.getAllUsers();
            List<Formulaire>          forms    = formulaireService.getAllForms();

            long totalReponses = forms.stream()
                    .mapToLong(f -> reponseService.countResponsesByForm(f.getId()))
                    .sum();

            Map<String, Object> dashboard = new HashMap<>();
            dashboard.put("totalUtilisateurs", users.size());
            dashboard.put("totalFormulaires",  forms.size());
            dashboard.put("totalReponses",     totalReponses);
            dashboard.put("utilisateurs",      users);
            dashboard.put("formulaires",       forms);

            return ResponseEntity.ok(dashboard);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Liste tous les utilisateurs
    @GetMapping("/utilisateurs")
    public ResponseEntity<List<UtilisateurResponse>> getAllUsers() {
        try {
            return ResponseEntity.ok(utilisateurService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Changer le rôle d'un utilisateur
    @PutMapping("/utilisateurs/{id}/role")
    public ResponseEntity<String> changerRole(
            @PathVariable int id,
            @RequestBody Map<String, String> body) {
        try {
            Utilisateur utilisateur = utilisateurRepository.findById(id).orElse(null);

            if (utilisateur == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Utilisateur introuvable");
            }

            String roleStr = body.get("role");
            utilisateur.setRole(Role.valueOf(roleStr));
            utilisateurRepository.save(utilisateur);

            return ResponseEntity.ok("Rôle mis à jour : " + roleStr);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur : " + e.getMessage());
        }
    }

    // Supprimer un utilisateur
    @DeleteMapping("/utilisateurs/{id}")
    public ResponseEntity<Void> supprimerUtilisateur(@PathVariable int id) {
        try {
            boolean deleted = utilisateurService.deleteUser(id);
            if (deleted) return ResponseEntity.noContent().build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Supprimer n'importe quel formulaire
    @DeleteMapping("/formulaires/{id}")
    public ResponseEntity<Void> supprimerFormulaire(@PathVariable int id) {
        try {
            formulaireService.deleteForm(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Stats par utilisateur
    @GetMapping("/utilisateurs/{id}/stats")
    public ResponseEntity<Map<String, Object>> getStatsUtilisateur(@PathVariable int id) {
        try {
            List<Formulaire> forms = formulaireService.getFormsByUser(id);

            long totalReponses = forms.stream()
                    .mapToLong(f -> reponseService.countResponsesByForm(f.getId()))
                    .sum();

            Map<String, Object> stats = new HashMap<>();
            stats.put("nbFormulaires", forms.size());
            stats.put("nbReponses",    totalReponses);
            stats.put("formulaires",   forms);

            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}