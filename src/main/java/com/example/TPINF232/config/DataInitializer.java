package com.example.TPINF232.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.TPINF232.model.Utilisateur;
import com.example.TPINF232.model.ENUM.Role;
import com.example.TPINF232.repository.UtilisateurRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public void run(String... args) {

        long count = utilisateurRepository.findAll()
            .stream()
            .filter(u -> u.getEmail().equals("josephkaisen66@gmail.com"))
            .count();

        if (count == 0) {
            Utilisateur admin = new Utilisateur();
            admin.setNom("Administrateur");
            admin.setEmail("josephkaisen66@gmail.com");
            admin.setNomUtilisateur("admin");
            admin.setMotDePasse("feuneck66");
            admin.setRole(Role.ADMIN);
            utilisateurRepository.save(admin);
            System.out.println("✅ Compte admin principal créé");
        } else {
            System.out.println("ℹ️ Compte admin principal déjà existant.");
        }
    }
}