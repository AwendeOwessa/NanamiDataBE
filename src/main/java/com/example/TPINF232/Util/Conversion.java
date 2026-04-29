package com.example.TPINF232.Util;


import org.springframework.stereotype.Component;

import com.example.TPINF232.dto.Utilisateur.utilisateur.UtilisateurResponse;
import com.example.TPINF232.model.Utilisateur;

@Component

public class Conversion {
    
    public UtilisateurResponse conversionUtilisateurToUtilisateurResponse(Utilisateur utilisateur) {

        UtilisateurResponse utilisateurResponse = new UtilisateurResponse();

        utilisateurResponse.setId(utilisateur.getId());
        utilisateurResponse.setNom(utilisateur.getNom());
        utilisateurResponse.setEmail(utilisateur.getEmail());
        utilisateurResponse.setNomUtilisateur(utilisateur.getNomUtilisateur());
        utilisateurResponse.setRole(utilisateur.getRole());

        return utilisateurResponse;
    }
    
    
}
