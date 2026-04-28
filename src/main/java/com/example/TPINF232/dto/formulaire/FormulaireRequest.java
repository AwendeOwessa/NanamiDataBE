package com.example.TPINF232.dto.formulaire;

import java.util.List;

import com.example.TPINF232.dto.question.QuestionRequest;

import lombok.Data;

@Data

public class FormulaireRequest {
    
    private String titre;
    private String description;
    private int utilisateurId;
    private List<QuestionRequest> questions;
}
