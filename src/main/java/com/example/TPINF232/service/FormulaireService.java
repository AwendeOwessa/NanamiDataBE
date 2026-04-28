package com.example.TPINF232.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TPINF232.dto.formulaire.FormulaireRequest;
import com.example.TPINF232.model.Formulaire;
import com.example.TPINF232.model.Question;
import com.example.TPINF232.model.Utilisateur;
import com.example.TPINF232.repository.FormulaireRepository;
import com.example.TPINF232.repository.QuestionRepository;
import com.example.TPINF232.repository.UtilisateurRepository;

import lombok.Data;

@Service
@Data

public class FormulaireService {

    @Autowired
    private FormulaireRepository formulaireRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private QuestionRepository questionRepository;

    // Creation d'un formulaire
    public Formulaire createForm(FormulaireRequest formulaireRequest) {

        Formulaire formulaire = new Formulaire();

        formulaire.setTitre(formulaireRequest.getTitre());
        formulaire.setDescription(formulaireRequest.getDescription());
        formulaire.setDateCreation(LocalDateTime.now());

        Utilisateur utilisateur = utilisateurRepository.findById(formulaireRequest.getUtilisateurId()).orElse(null);

        formulaire.setUtilisateur(utilisateur);

        Formulaire savedFormulaire = formulaireRepository.save(formulaire);

        if (formulaireRequest.getQuestions() != null) {

            formulaireRequest.getQuestions().forEach(questionRequest -> {

            Question question = new Question();

            question.setLabel(questionRequest.getLabel());
            question.setType(questionRequest.getType());
            question.setOptions(questionRequest.getOptions());
            question.setFormulaire(savedFormulaire);
    
            questionRepository.save(question);
        });
        }

        return formulaireRepository.findById(savedFormulaire.getId()).orElse(savedFormulaire);
    }

    // Mise à jour d'un formulaire
    public Formulaire updateForm(int id, FormulaireRequest formulaireRequest) {

        Formulaire formulaire = formulaireRepository.findById(id).orElse(null);

        if (formulaire != null) {
            formulaire.setTitre(formulaireRequest.getTitre());
            formulaire.setDescription(formulaireRequest.getDescription());

            return formulaireRepository.save(formulaire);
        }

        return null;
    }
    
    // Suppression d'un formulaire
    public boolean deleteForm(int id) {

        if (formulaireRepository.existsById(id)) {
            formulaireRepository.deleteById(id);
            return true;
        }

        return false;
    }

    // Récupérer un formulaire par son ID
    public Formulaire getFormById(int id) {

        return formulaireRepository.findById(id).orElse(null);
    }
    
    // Récupérer tous les formulaires
    public List<Formulaire> getAllForms() {

        return formulaireRepository.findAll();
    }

    // Récupérer les formulaires d'un utilisateur
    public List<Formulaire> getFormsByUser(int utilisateurId) {

        return formulaireRepository.findByUtilisateurId(utilisateurId);
    }
}
