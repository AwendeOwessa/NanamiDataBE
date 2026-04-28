package com.example.TPINF232.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TPINF232.dto.contact.ContactRequest;
import com.example.TPINF232.service.MailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private MailService mailService;

    @PostMapping("/envoyer")
    public ResponseEntity<String> envoyerContact(@Valid @RequestBody ContactRequest request) {
    
        try {
            mailService.envoyerMessageContact(request);
            return ResponseEntity.ok("Message envoyé avec succès");
    
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("Erreur serveur: " + e.getMessage());
        }
    }
}