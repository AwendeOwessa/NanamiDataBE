package com.example.TPINF232.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.TPINF232.dto.contact.ContactRequest;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.mail.destinataire}")
    private String destinataire;

    @Value("${spring.mail.username}")
    private String expediteur;

    public void envoyerMessageContact(ContactRequest request) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(expediteur);
        helper.setTo(destinataire);
        helper.setReplyTo(request.getEmail());

        helper.setSubject("[NanamiData] " + request.getSujet() + " - " + request.getNom());

        String contenu = construireCorpsMessage(request);
        helper.setText(contenu, true);

        mailSender.send(message);

        if (request.isCopie()) {
            envoyerConfirmation(request);
        }
    }

    private void envoyerConfirmation(ContactRequest request) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(expediteur);
        helper.setTo(request.getEmail());

        // ← retire aussi le tiret — ici
        helper.setSubject("Confirmation - Votre message a bien ete recu");

        String contenu = construireConfirmation(request);
        helper.setText(contenu, true);

        mailSender.send(message);
    }

    private String construireCorpsMessage(ContactRequest request) {
        return """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;
                        background: #0f172a; color: #e2e8f0; padding: 32px; border-radius: 12px;">
                
                <div style="border-bottom: 2px solid #38bdf8; padding-bottom: 16px; margin-bottom: 24px;">
                    <h2 style="color: #38bdf8; margin: 0;">Nouveau message de contact</h2>
                    <p style="color: #64748b; margin: 4px 0 0;">NanamiData - INF232</p>
                </div>

                <table style="width: 100%%; border-collapse: collapse;">
                    <tr>
                        <td style="padding: 8px 0; color: #94a3b8; width: 120px;">Nom</td>
                        <td style="padding: 8px 0; color: #f1f5f9; font-weight: bold;">%s</td>
                    </tr>
                    <tr>
                        <td style="padding: 8px 0; color: #94a3b8;">Email</td>
                        <td style="padding: 8px 0;">
                            <a href="mailto:%s" style="color: #38bdf8;">%s</a>
                        </td>
                    </tr>
                    <tr>
                        <td style="padding: 8px 0; color: #94a3b8;">Sujet</td>
                        <td style="padding: 8px 0; color: #f1f5f9;">%s</td>
                    </tr>
                </table>

                <div style="margin-top: 24px; padding: 20px;
                            background: #1e293b; border-radius: 10px;
                            border-left: 4px solid #38bdf8;">
                    <p style="color: #94a3b8; font-size: 13px; margin: 0 0 10px;">Message</p>
                    <p style="color: #e2e8f0; line-height: 1.7; margin: 0;">%s</p>
                </div>

                <div style="margin-top: 24px; text-align: center; color: #334155; font-size: 12px;">
                    <p>Envoye depuis le formulaire de contact NanamiData</p>
                </div>
            </div>
            """.formatted(
                request.getNom(),
                request.getEmail(), request.getEmail(),
                request.getSujet(),
                request.getMessage()
            );
    }

    private String construireConfirmation(ContactRequest request) {
        return """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;
                        background: #0f172a; color: #e2e8f0; padding: 32px; border-radius: 12px;">

                <div style="text-align: center; margin-bottom: 32px;">
                    <h1 style="color: #38bdf8; font-size: 28px;">Message recu !</h1>
                    <p style="color: #64748b;">Merci de nous avoir contactes, %s.</p>
                </div>

                <div style="padding: 20px; background: #1e293b;
                            border: 1px solid #38bdf8; border-radius: 10px;
                            margin-bottom: 24px;">
                    <p style="color: #94a3b8; font-size: 13px; margin: 0 0 6px;">
                        Votre message concernant
                    </p>
                    <p style="color: #38bdf8; font-weight: bold; margin: 0;">%s</p>
                </div>

                <p style="color: #64748b; line-height: 1.7;">
                    Nous avons bien recu votre message et nous vous repondrons
                    a l'adresse <strong style="color: #38bdf8;">%s</strong>.
                </p>

                <div style="margin-top: 32px; padding-top: 20px;
                            border-top: 1px solid #1e293b;
                            text-align: center; color: #334155; font-size: 12px;">
                    <p>2026 NanamiData - INF232</p>
                </div>
            </div>
            """.formatted(
                request.getNom(),
                request.getSujet(),
                request.getEmail()
            );
    }
}