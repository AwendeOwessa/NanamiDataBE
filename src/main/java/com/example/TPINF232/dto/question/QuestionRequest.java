package com.example.TPINF232.dto.question;

import java.util.List;

import com.example.TPINF232.model.ENUM.QuestionType;

import lombok.Data;

@Data

public class QuestionRequest {
    
    private String label;
    private QuestionType type;
    private List<String> options;
    private int formulaireId;
    
}

