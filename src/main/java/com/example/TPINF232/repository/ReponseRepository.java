package com.example.TPINF232.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.TPINF232.model.Reponse;

public interface ReponseRepository extends JpaRepository<Reponse, Integer> {
    
    // Compte du nombre de réponses pour un formulaire donné
    long countByFormulaireId(int formId);

    // Récupération de toutes les réponses pour un formulaire donné
    List<Reponse> findByFormulaireId(int formId);
}