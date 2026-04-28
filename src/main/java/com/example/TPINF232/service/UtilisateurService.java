package com.example.TPINF232.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TPINF232.Util.Conversion;
import com.example.TPINF232.dto.Utilisateur.utilisateur.UtilisateurLogin;
import com.example.TPINF232.dto.Utilisateur.utilisateur.UtilisateurRequest;
import com.example.TPINF232.dto.Utilisateur.utilisateur.UtilisateurResponse;
import com.example.TPINF232.model.Utilisateur;
import com.example.TPINF232.model.ENUM.Role;
import com.example.TPINF232.repository.UtilisateurRepository;
import com.example.TPINF232.security.JwtUtil;

import lombok.Data;

@Service
@Data

public class UtilisateurService {
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private Conversion conversion;

    //Enregistrement d'un utilisateur
    public UtilisateurResponse registerUtilisateur(UtilisateurRequest utilisateurRequest) {

        Utilisateur utilisateur = new Utilisateur();

        utilisateur.setNom(utilisateurRequest.getNom());
        utilisateur.setEmail(utilisateurRequest.getEmail());
        utilisateur.setNomUtilisateur(utilisateurRequest.getNomUtilisateur());
        utilisateur.setMotDePasse(utilisateurRequest.getMotDePasse());
        utilisateur.setRole(Role.USER);

        return conversion.conversionUtilisateurToUtilisateurResponse(utilisateurRepository.save(utilisateur));
    }

    //Connexion d'un utilisateur
    public UtilisateurResponse loginUtilisateur(UtilisateurLogin utilisateurLogin) {

        Utilisateur utilisateur = utilisateurRepository.findByEmailAndMotDePasse(
            utilisateurLogin.getEmail(),
            utilisateurLogin.getMotDePasse()
        );

        if (utilisateur == null) return null;

        if (utilisateur.getRole() == Role.USER) {
            boolean estPremierAutreCompte = estPremierCompteNonAdmin(utilisateur);
            if (estPremierAutreCompte) {
                utilisateur.setRole(Role.ADMIN);
                utilisateurRepository.save(utilisateur);
                System.out.println("✅ Promotion automatique en ADMIN : " + utilisateur.getEmail());
            }
        }

        UtilisateurResponse response = conversion
            .conversionUtilisateurToUtilisateurResponse(utilisateur);

        response.setToken(JwtUtil.generateToken(
            utilisateur.getEmail(),
            utilisateur.getRole().name()
        ));

        return response;
    }

    private boolean estPremierCompteNonAdmin(Utilisateur utilisateur) {

        long nbUsers = utilisateurRepository.findAll()
            .stream()
            .filter(u -> u.getRole() == Role.USER
                    && u.getId() != utilisateur.getId()
                    && !u.getEmail().equals("josephkaisen66@gmail.com"))
            .count();

        return nbUsers == 0;
    }

    public UtilisateurResponse findUserById(int id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElse(null);

        if (utilisateur != null) {
            
            return conversion.conversionUtilisateurToUtilisateurResponse(utilisateur);
        }
        return null;
    }
    
    //Trouver un utilisateur par son email
    public UtilisateurResponse findUserByEmail(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email);
        
        if (utilisateur != null) {

            return conversion.conversionUtilisateurToUtilisateurResponse(utilisateur);
        }
        return null;
    }

    //Récupérer tous les utilisateurs
    public List<UtilisateurResponse> getAllUsers() {

        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();

        return utilisateurs.stream().map(utilisateur -> conversion.conversionUtilisateurToUtilisateurResponse(utilisateur)).toList();
    }

    //Supprimer un utilisateur par son ID
    public boolean deleteUser(int id) {
        
        if (utilisateurRepository.existsById(id)) {
            utilisateurRepository.deleteById(id);
            return true;
        }
        
        return false;
    }
}
