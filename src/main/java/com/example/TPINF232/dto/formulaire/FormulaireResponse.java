package com.example.TPINF232.dto.formulaire;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class FormulaireResponse {

    private int           id;
    private String        titre;
    private String        description;
    private LocalDateTime dateCreation;
    private AuteurInfo    utilisateur;
    private List<QuestionInfo> questions;

    @Data
    public static class AuteurInfo {
        private int    id;
        private String nom;
        private String nomUtilisateur;
    }

    @Data
    public static class QuestionInfo {
        private int          id;
        private String       label;
        private String       type;
        private List<String> options;
    }
}