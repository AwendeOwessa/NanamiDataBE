package com.example.TPINF232.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TPINF232.dto.reponse.ReponseRequest;
import com.example.TPINF232.model.Answer;
import com.example.TPINF232.model.Formulaire;
import com.example.TPINF232.model.Question;
import com.example.TPINF232.model.Reponse;
import com.example.TPINF232.repository.AnswerRepository;
import com.example.TPINF232.repository.ReponseRepository;

import lombok.Data;

@Service
@Data
public class ReponseService {

    @Autowired
    private ReponseRepository reponseRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private FormulaireService formulaireService;

    @Autowired
    private QuestionService questionService;

    // Soumission d'une réponse à un formulaire
    public Reponse submitReponse(ReponseRequest reponseRequest) {

        Formulaire formulaire = formulaireService.getFormById(reponseRequest.getFormulaireId());

        if (formulaire == null) {
            throw new RuntimeException("Formulaire introuvable : " + reponseRequest.getFormulaireId());
        }

        Reponse reponse = new Reponse();
        reponse.setFormulaire(formulaire);
        reponse.setDateDeSoumission(LocalDateTime.now());

        Reponse savedReponse = reponseRepository.save(reponse);

        reponseRequest.getAnswers().forEach(answerData -> {

            Question question = questionService.getQuestionById(answerData.getQuestionId());

            if (question == null) {
                throw new RuntimeException("Question introuvable : " + answerData.getQuestionId());
            }

            Answer answer = new Answer();
            answer.setValeur(answerData.getValeur());
            answer.setQuestion(question);
            answer.setReponse(savedReponse);

            answerRepository.save(answer);
        });

        return savedReponse;
    }

    // Récupération de toutes les réponses pour un formulaire donné
    public List<Reponse> getResponsesByForm(int formId) {

        return reponseRepository.findByFormulaireId(formId);
    }

    // Récupération d'une réponse par son ID
    public Reponse getResponseById(int id) {

        return reponseRepository.findById(id).orElse(null);
    }

    // Suppression d'une réponse par son ID
    public boolean deleteResponse(int id) {

        if (!reponseRepository.existsById(id)) {
            return false;
        }
        
        reponseRepository.deleteById(id);

        return true;
    }

    // Comptage du nombre de réponses pour un formulaire donné
    public long countResponsesByForm(int formId) {

        return reponseRepository.countByFormulaireId(formId);
    }
}