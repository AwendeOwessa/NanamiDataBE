package com.example.TPINF232.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TPINF232.dto.question.QuestionRequest;
import com.example.TPINF232.model.Formulaire;
import com.example.TPINF232.model.Question;
import com.example.TPINF232.repository.FormulaireRepository;
import com.example.TPINF232.repository.QuestionRepository;

import lombok.Data;

@Service
@Data

public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private FormulaireService formulaireService;
    
    @Autowired
    private FormulaireRepository formulaireRepository;

    // Ajouter une question à un formulaire
    public Formulaire addQuestionToForm(QuestionRequest questionRequest) {

        Formulaire formulaire = formulaireRepository.findById(questionRequest.getFormulaireId()).orElseThrow(() -> new RuntimeException("Formulaire non trouvé"));

        Question question = new Question();
        
        question.setLabel(questionRequest.getLabel());
        question.setType(questionRequest.getType());
        question.setOptions(questionRequest.getOptions());
        question.setFormulaire(formulaire);

        questionRepository.save(question);  

        return formulaireRepository.findById(formulaire.getId()).orElse(formulaire);
    }

    // Mettre à jour une question
    public Question updateQuestion(int id, QuestionRequest questionRequest) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question non trouvée"));

        question.setLabel(questionRequest.getLabel());
        question.setType(questionRequest.getType());
        question.setOptions(questionRequest.getOptions());

        return questionRepository.save(question);
    }
    
    // Supprimer une question
    public boolean deleteQuestion(int id) {

        Question question = questionRepository.findById(id).orElse(null);

        if (question == null) {
            return false;
        }

        questionRepository.delete(question);

        return true;
    }
    
    // Récupérer les questions d'un formulaire
    public List<Question> getQuestionsByForm(int formId) {
        
        return questionRepository.findByFormulaireId(formId);
    }

    // Récupérer une question par son ID
    public Question getQuestionById(int id) {
        
        return questionRepository.findById(id).orElse(null);
    }
}
