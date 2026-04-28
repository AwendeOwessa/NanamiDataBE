package com.example.TPINF232.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TPINF232.model.Formulaire;

@Repository

public interface FormulaireRepository extends JpaRepository<Formulaire, Integer> {
    
    // Trouver les formulaires par utilisateur
    List<Formulaire> findByUtilisateurId(int utilisateurId);
}
