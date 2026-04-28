package com.example.TPINF232.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TPINF232.model.Utilisateur;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    
    //Méthode pour trouver un utilisateur par email et mot de passe
    Utilisateur findByEmailAndMotDePasse(String email, String motDePasse);

    //Méthode pour trouver un utilisateur par email
    Utilisateur findByEmail(String email);
}
