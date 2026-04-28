package com.example.TPINF232.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.TPINF232.dto.question.QuestionRequest;
import com.example.TPINF232.model.Formulaire;
import com.example.TPINF232.model.Question;
import com.example.TPINF232.service.QuestionService;

import lombok.Data;

@RestController
@Data
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    // Ajouter une question à un formulaire
    @PostMapping("/add")
    public ResponseEntity<Formulaire> addQuestion(@RequestBody QuestionRequest questionRequest) {
        try {
            Formulaire formulaire = questionService.addQuestionToForm(questionRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(formulaire);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Mettre à jour une question
    @PutMapping("/update/{id}")
    public ResponseEntity<Question> updateQuestion(@RequestBody QuestionRequest questionRequest,
                                                    @PathVariable int id) {
        try {
            Question updatedQuestion = questionService.updateQuestion(id, questionRequest);
            return ResponseEntity.ok(updatedQuestion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Supprimer une question
    @DeleteMapping("/delete/{id}")     // ← était @PostMapping
    public ResponseEntity<Void> deleteQuestion(@PathVariable int id) {
        try {
            questionService.deleteQuestion(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Récupérer les questions d'un formulaire
    @GetMapping("/form/{formId}")      // ← était @PostMapping
    public ResponseEntity<List<Question>> getQuestionsByForm(@PathVariable int formId) {
        try {
            List<Question> questions = questionService.getQuestionsByForm(formId);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Récupérer une question par son ID
    @GetMapping("/{id}")               // ← était @PostMapping
    public ResponseEntity<Question> getQuestionById(@PathVariable int id) {
        try {
            Question question = questionService.getQuestionById(id);
            if (question != null) {
                return ResponseEntity.ok(question);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}