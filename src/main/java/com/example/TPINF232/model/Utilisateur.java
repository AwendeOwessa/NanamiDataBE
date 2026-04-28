package com.example.TPINF232.model;

import com.example.TPINF232.model.ENUM.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data

public class Utilisateur {
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    
    private String nom;
    private String email;
    private String nomUtilisateur;
    private String motDePasse;
    private Role role;

}
