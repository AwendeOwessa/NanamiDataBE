package com.example.TPINF232.dto.Utilisateur.utilisateur;

import com.example.TPINF232.model.ENUM.Role;

import lombok.Data;

@Data

public class UtilisateurResponse {
    
    private int id;
    private String nom;
    private String email;
    private String nomUtilisateur;
    private Role role;
    private String token;
}
