package com.example.vortex_games.auth;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail,
                                String subject,
                                String nombre,
                                String apellido
    ) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);

            String htmlBody =
                    "<html>"
                            + "<body>"
                            + "<h1 style=\"color:blue;\">¡Gracias por registrarte "
                            + nombre + " " + apellido + "!</h1>"
                            + "<h2>¡Tu cuenta ha sido creada exitosamente!</h2>"
                            + "<h3>" +
                            "Accede a ella desde este "
                            + "<a href="+"http://despliegue-front-phi.vercel.app/"+">Link</a> "+"</h3>"
                            //+ "<img src='cid:Logo'/>"
                            + "</body>"
                            + "</html>";

            helper.setText(htmlBody, true); // Set true to indicate HTML content

//            helper.addInline("Logo", new ClassPathResource("../../../main/resources/fondoblanco.png"));
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle exception
        }
        mailSender.send(message);

    }


    public void sendEmailReserva(String toEmail, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true); // Set true to indicate HTML content
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle exception
        }
        mailSender.send(message);
    }

}