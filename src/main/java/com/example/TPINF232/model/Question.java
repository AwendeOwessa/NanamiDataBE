package com.example.TPINF232.model;

import java.util.List;

import com.example.TPINF232.model.ENUM.QuestionType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data

public class Question {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;

    private String label;
    private QuestionType type;

    @ManyToOne
    @JsonIgnore
    private Formulaire formulaire;

    private List<String> options;
}
