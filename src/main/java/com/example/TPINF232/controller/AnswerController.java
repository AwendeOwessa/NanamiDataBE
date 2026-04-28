package com.example.TPINF232.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TPINF232.dto.answer.AnswerRequest;
import com.example.TPINF232.model.Answer;
import com.example.TPINF232.service.AnswerService;

import lombok.Data;

@RestController
@Data
@RequestMapping("/api/answers")

public class AnswerController {

    @Autowired
    private AnswerService answerService;

    // Enregistrement d'une réponse individuelle
    @PostMapping("/save")
    public ResponseEntity<Answer> saveAnswer(@RequestBody AnswerRequest answerRequest) {
        
        try {
        
            return ResponseEntity.status(HttpStatus.CREATED).body(answerService.saveAnswer(answerRequest));
        
        } catch (Exception e) {
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Récupération de toutes les réponses individuelles pour une question donnée
    @GetMapping("/question/{questionId}")
    public ResponseEntity<?> getAnswersByQuestion(@PathVariable int questionId) {
    
        try {
    
            return ResponseEntity.ok(answerService.getAnswersByQuestion(questionId));
    
        } catch (Exception e) {
    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Récupération de toutes les réponses individuelles pour une réponse donnée
    @GetMapping("/response/{responseId}")
    public ResponseEntity<?> getAnswersByResponse(@PathVariable int responseId) {
    
        try {
    
            return ResponseEntity.ok(answerService.getAnswersByResponse(responseId));
    
        } catch (Exception e) {
    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }   
    

    // Suppression d'une réponse individuelle par son ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteAnswer(@PathVariable int id) {

        try {

            answerService.deleteAnswer(id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
