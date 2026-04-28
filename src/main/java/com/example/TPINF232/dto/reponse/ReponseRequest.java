package com.example.TPINF232.dto.reponse;

import java.util.List;

import com.example.TPINF232.dto.answer.AnswerData;

import lombok.Data;

@Data
public class ReponseRequest {

    private int formulaireId;
    private int utilisateurId;
    private List<AnswerData> answers;

    private  List<AnswerData> answerDatas;
}
