package com.example.TPINF232.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TPINF232.model.Answer;

@Repository

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    
    // Récupération de toutes les réponses individuelles pour une question donnée
    List<Answer> findByReponseId(int reponseId);

    // Récupération de toutes les réponses individuelles pour une réponse donnée
    List<Answer> findByQuestionId(int questionId);

}