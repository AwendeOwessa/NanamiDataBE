package com.example.TPINF232.dto.answer;

import lombok.Data;

@Data

public class AnswerRequest {

    private String valeur;
    private int questionId;
    private int reponseId;
}
