package com.example.TPINF232.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TPINF232.dto.answer.AnswerRequest;
import com.example.TPINF232.model.Answer;
import com.example.TPINF232.repository.AnswerRepository;
import com.example.TPINF232.repository.ReponseRepository;

import lombok.Data;

@Service
@Data

public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ReponseService reponseService;
    @Autowired
    private ReponseRepository reponseRepository;

    @Autowired
    private QuestionService questionService;

    // Enregistrement d'une réponse individuelle
    public Answer saveAnswer(AnswerRequest answerRequest) {

        Answer answer = new Answer();
        
        answer.setValeur(answerRequest.getValeur());
        answer.setQuestion(questionService.getQuestionById(answerRequest.getQuestionId()));
        answer.setReponse(reponseService.getResponseById(answerRequest.getReponseId()));

        return answerRepository.save(answer);
    }

    // Récupération de toutes les réponses individuelles pour une question donnée
    public List<Answer> getAnswersByQuestion(int questionId) {
        
        return answerRepository.findByQuestionId(questionId);
    } 

    // Récupération de toutes les réponses individuelles pour une réponse donnée
    public List<Answer> getAnswersByResponse(int reponseId) {

        return answerRepository.findByReponseId(reponseId);
    }
    
    // Suppression d'une réponse individuelle par son ID
    public boolean deleteAnswer(int id){

        boolean answerExists = answerRepository.existsById(id);

        if (!answerExists) {
            return false;
        }

        answerRepository.deleteById(id);

        return true;
    }

}
