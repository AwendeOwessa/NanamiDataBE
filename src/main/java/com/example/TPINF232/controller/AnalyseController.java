package com.example.TPINF232.controller;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.TPINF232.model.Reponse;
import com.example.TPINF232.service.AnalyseService;
import com.example.TPINF232.service.ReponseService;

import lombok.Data;

@RestController
@Data
@RequestMapping("/api/analyse")

public class AnalyseController {
    
    @Autowired
    private AnalyseService analyseService;
    
    @Autowired
    private ReponseService reponseService;
    // Comptage des réponses pour une question donnée
    @GetMapping("/countAnswersByQuestion")
    public ResponseEntity<Map<String, Long>> countAnswersByQuestion(@RequestParam int questionId) {

        try {

            Map<String, Long> result = analyseService.countAnswersByQuestion(questionId);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Calcul des pourcentages de réponses pour une question donnée
    @GetMapping("/calculateAnswerPercentagesByQuestion")
    public ResponseEntity<Map<String, Double>> calculateAnswerPercentagesByQuestion(@RequestParam int questionId) {

        try {

            Map<String, Double> result = analyseService.calculateAnswerPercentagesByQuestion(questionId);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Moyenne des réponses pour une question donnée
    @GetMapping("/calculateAverage")
    public ResponseEntity<Double> calculateAverage(@RequestParam int questionId) {

        try {

            double result = analyseService.calculateAverage(questionId);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Distribution des réponses pour une question donnée
    @GetMapping("/calculateDistribution")
    public ResponseEntity<Map<Integer, Long>> calculateDistribution(@RequestParam int questionId) {

        try {

            Map<Integer, Long> result = analyseService.getAnswerDistribution(questionId);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Gestion du choix multiple pour une question donnée
    @GetMapping("/countMultipleChoice")
    public ResponseEntity<Map<String, Long>> countMultipleChoice(@RequestParam int questionId) {

        try {

            Map<String, Long> result = analyseService.countMultipleChoice(questionId);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Analyse des réponses pour un formulaire donné
    @GetMapping("/analyseFormulaire")
    public ResponseEntity<Map<String, Object>> analyseFormulaire(@RequestParam int formulaireId) {

        try {

            Map<String, Object> result = analyseService.getFormAnalytics(formulaireId);

            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Evolution des réponses dans le temps
    @GetMapping("/evolution/{formulaireId}")
    public ResponseEntity<List<Map<String, Object>>> getEvolution(@PathVariable int formulaireId) {
        try {
            List<Reponse> reponses = reponseService.getResponsesByForm(formulaireId);

            Map<String, Long> parJour = reponses.stream()
                    .collect(Collectors.groupingBy(
                            r -> r.getDateDeSoumission()
                                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            Collectors.counting()
                    ));

            List<Map<String, Object>> evolution = parJour.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> {
                        Map<String, Object> point = new HashMap<>();
                        point.put("date",  e.getKey());
                        point.put("count", e.getValue());
                        return point;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(evolution);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
