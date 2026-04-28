package com.example.TPINF232.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TPINF232.model.Question;

@Repository

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    
    //Recuperer les questions d'un formulaire
    List<Question> findByFormulaireId(int formulaireId); 
}
