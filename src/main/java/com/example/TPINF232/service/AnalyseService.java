package com.example.TPINF232.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TPINF232.model.Answer;
import com.example.TPINF232.model.Question;
import com.example.TPINF232.model.Reponse;

import lombok.Data;

@Service
@Data

public class AnalyseService {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ReponseService reponseService;

    @Autowired
    private FormulaireService formulaireService;


    private List<Double> extraireValeursNumeriques(int questionId) {

        return answerService.getAnswersByQuestion(questionId).stream()
                .map(Answer::getValeur)
                .filter(v -> v != null && !v.isBlank())
                .flatMap(v -> {
                    try {
                        return java.util.stream.Stream.of(Double.parseDouble(v));
                    } catch (NumberFormatException e) {
                        return java.util.stream.Stream.empty();
                    }
                })
                .collect(Collectors.toList());
    }

    public double calculateAverage(int questionId) {
        List<Double> valeurs = extraireValeursNumeriques(questionId);
        if (valeurs.isEmpty()) return 0;
        return valeurs.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    public double calculateMin(int questionId) {
        List<Double> valeurs = extraireValeursNumeriques(questionId);
        if (valeurs.isEmpty()) return 0;
        return Collections.min(valeurs);
    }

    public double calculateMax(int questionId) {
        List<Double> valeurs = extraireValeursNumeriques(questionId);
        if (valeurs.isEmpty()) return 0;
        return Collections.max(valeurs);
    }

    public double calculateMediane(int questionId) {
        List<Double> valeurs = extraireValeursNumeriques(questionId);
        if (valeurs.isEmpty()) return 0;

        Collections.sort(valeurs);
        int n = valeurs.size();

        if (n % 2 == 0) {
            return (valeurs.get(n / 2 - 1) + valeurs.get(n / 2)) / 2.0;
        } else {
            return valeurs.get(n / 2);
        }
    }

    public double calculateEcartType(int questionId) {
        List<Double> valeurs = extraireValeursNumeriques(questionId);
        if (valeurs.size() < 2) return 0;

        double moyenne = calculateAverage(questionId);
        double variance = valeurs.stream()
                .mapToDouble(v -> Math.pow(v - moyenne, 2))
                .average()
                .orElse(0);

        return Math.sqrt(variance);
    }

    public double calculateCV(int questionId) {
        double moyenne   = calculateAverage(questionId);
        double ecartType = calculateEcartType(questionId);
        if (moyenne == 0) return 0;
        return (ecartType / moyenne) * 100;
    }

    private String interpreterCV(double cv) {
        if (cv < 15)  return "Homogène";
        if (cv < 30)  return "Modérément variable";
        return "Très variable";
    }

    public Map<String, Object> getStatistiquesDescriptives(int questionId) {

        List<Double> valeurs = extraireValeursNumeriques(questionId);
        double moyenne       = calculateAverage(questionId);
        double ecartType     = calculateEcartType(questionId);
        double cv            = calculateCV(questionId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("nValides",      valeurs.size());
        stats.put("min",           valeurs.isEmpty() ? null : calculateMin(questionId));
        stats.put("max",           valeurs.isEmpty() ? null : calculateMax(questionId));
        stats.put("moyenne",       moyenne);
        stats.put("mediane",       calculateMediane(questionId));
        stats.put("ecartType",     ecartType);
        stats.put("cv",            Math.round(cv * 100.0) / 100.0);
        stats.put("interpretation",interpreterCV(cv));

        return stats;
    }

    public Map<String, Long> getDistributionParTranches(int questionId, int tailleTrancheInt) {

        List<Double> valeurs = extraireValeursNumeriques(questionId);
        if (valeurs.isEmpty()) return new HashMap<>();

        double min          = Collections.min(valeurs);
        double max          = Collections.max(valeurs);
        double tailleTranche = tailleTrancheInt;
        Map<String, Long> distribution = new HashMap<>();

        for (double debut = Math.floor(min / tailleTranche) * tailleTranche;
             debut <= max;
             debut += tailleTranche) {

            double fin      = debut + tailleTranche;
            String tranche  = (int) debut + "–" + (int) fin;
            double debutFin = debut;

            long count = valeurs.stream()
                    .filter(v -> v >= debutFin && v < debutFin + tailleTranche)
                    .count();

            distribution.put(tranche, count);
        }

        return distribution;
    }

    public Map<String, Long> countAnswersByQuestion(int questionId) {
        List<Answer> answers = answerService.getAnswersByQuestion(questionId);
        return answers.stream()
                .collect(Collectors.groupingBy(Answer::getValeur, Collectors.counting()));
    }

    public Map<String, Double> calculateAnswerPercentagesByQuestion(int questionId) {

        Map<String, Long> answerCounts = countAnswersByQuestion(questionId);
        long total = answerCounts.values().stream().mapToLong(Long::longValue).sum();

        Map<String, Double> pourcentages = new HashMap<>();
        answerCounts.forEach((valeur, count) -> {
            double pct = (double) count / total * 100;
            pourcentages.put(valeur, Math.round(pct * 100.0) / 100.0);
        });

        return pourcentages;
    }

    public Map<String, Long> countMultipleChoice(int questionId) {

        List<Answer> answers = answerService.getAnswersByQuestion(questionId);
        Map<String, Long> choiceCounts = new HashMap<>();

        for (Answer answer : answers) {
            String[] choices = answer.getValeur().split(",");
            for (String choice : choices) {
                String c = choice.trim();
                choiceCounts.put(c, choiceCounts.getOrDefault(c, 0L) + 1);
            }
        }

        return choiceCounts;
    }

    public Map<Integer, Long> getAnswerDistribution(int questionId) {

        List<Answer> answers = answerService.getAnswersByQuestion(questionId);

        return answers.stream()
                .filter(a -> {
                    try { Integer.parseInt(a.getValeur()); return true; }
                    catch (NumberFormatException e) { return false; }
                })
                .collect(Collectors.groupingBy(
                        a -> Integer.parseInt(a.getValeur()),
                        Collectors.counting()
                ));
    }


    public Map<String, Object> getFormAnalytics(int formulaireId) {

        List<Question> questions = questionService.getQuestionsByForm(formulaireId);
        Map<String, Object> resultats = new HashMap<>();

        for (Question question : questions) {

            Map<String, Object> analyse = new HashMap<>();
            analyse.put("id", question.getId());
            analyse.put("texte", question.getLabel());
            analyse.put("type", question.getType());

            switch (question.getType()) {

                case CHOIX_UNIQUE:
                    analyse.put("comptage", countAnswersByQuestion(question.getId()));
                    analyse.put("pourcentages", calculateAnswerPercentagesByQuestion(question.getId()));
                    break;

                case CHOIX_MULTIPLE:
                    analyse.put("comptage", countMultipleChoice(question.getId()));
                    break;

                case TEXTE:
                    List<String> reponses = answerService.getAnswersByQuestion(question.getId())
                            .stream().map(Answer::getValeur).collect(Collectors.toList());
                    analyse.put("reponses", reponses);
                    analyse.put("total", reponses.size());
                    break;

                case VRAI_FAUX:
                    analyse.put("comptage", countAnswersByQuestion(question.getId()));
                    analyse.put("pourcentages", calculateAnswerPercentagesByQuestion(question.getId()));
                    break;

                case ECHELLE:
                    analyse.put("distribution", getAnswerDistribution(question.getId()));
                    analyse.put("statistiques", getStatistiquesDescriptives(question.getId()));
                    break;

                case NOMBRE:
                    analyse.put("statistiques", getStatistiquesDescriptives(question.getId()));
                    analyse.put("distribution", getDistributionParTranches(question.getId(), 10));
                    break;

                default:
                    break;
            }

            resultats.put(question.getLabel(), analyse);
        }

        return resultats;
    }
    
    // Evolution des réponses dans le temps
    public List<Reponse> getResponsesByForm(int formulaireId) {
        
        return reponseService.getResponsesByForm(formulaireId);
    }
}