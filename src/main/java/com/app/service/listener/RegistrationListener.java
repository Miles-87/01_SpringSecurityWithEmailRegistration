package com.app.service.listener;

import com.app.exception.MyException;
import com.app.model.security.User;
import com.app.model.security.VerificationToken;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

// ta klasa przechwyci zdarzenie ktore przekaze obiekt
// OnRegistrationCompleteEvent
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrartionCompleteEvent> {

    private UserService userService;
    private JavaMailSender javaMailSender;

    public RegistrationListener(
            UserService userService,
            @Qualifier("javaMailSender") JavaMailSender javaMailSender) {
        this.userService = userService;
        this.javaMailSender = javaMailSender;
    }

    // ta metoda zostanie wywolana kiedy bedzie wygenerowane zdarzenie
    // z obiektem ObRegistrationCompleteEvent
    @Override
    public void onApplicationEvent(OnRegistrartionCompleteEvent onRegistrartionCompleteEvent) {
        generateTokenAndSendEmail(onRegistrartionCompleteEvent);
    }

    private void generateTokenAndSendEmail(OnRegistrartionCompleteEvent event) {
        try {
            // wygeneroanie tokena dla nowego usera i zapisanie do bazy do tabeli
            // tokens
            User user = event.getUser();
            VerificationToken verificationToken = userService.verificationToken(user);

            // wyslanie maila z wygenerowanym tokenem
            String recipientAddress = user.getEmail();
            String subject = "Registration confirmation";
            String confirmationUrl = event.getUrl() + "users/registrationConfirm?token=" + verificationToken.getToken();
            String message = "CLICK TO ACTIVATE: " + confirmationUrl;

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(recipientAddress);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(message);
            javaMailSender.send(simpleMailMessage);

        } catch (Exception e) {
            throw new MyException("GENERATE TOKEN AND SEN EMAIL EXCEPTION", LocalDateTime.now());
        }
    }
}
