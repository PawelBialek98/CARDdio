package pl.lodz.p.it.cardio.events.accountOperation;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.cardio.dto.UserDto.UserDto;
import pl.lodz.p.it.cardio.exception.AppNotFoundException;
import pl.lodz.p.it.cardio.exception.EmailException;
import pl.lodz.p.it.cardio.service.UserService;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class AccountOperationListener implements
        ApplicationListener<AccountOperationEvent> {

    private final UserService service;

    @Qualifier("messageSource")
    private final MessageSource messages;

    private final JavaMailSender mailSender;

    @Value("${url}")
    private String appUrl;

    @SneakyThrows
    @Override
    public void onApplicationEvent(AccountOperationEvent event) {
        UserDto user = event.getUser();
        String token = UUID.randomUUID().toString();
        try {
            service.createVerificationToken(user, token, event.getMessagePrefix());
        } catch (AppNotFoundException e) {
            Logger.getGlobal().log(Level.INFO, e.getMessage());
        }

        String recipientAddress = user.getEmail();
        String subject = messages.getMessage(event.getMessagePrefix() + ".title", null, event.getLocale());
        String confirmationUrl
                = event.getAppUrl() + "/" +
                messages.getMessage(event.getMessagePrefix() + ".link", null, event.getLocale())
                + "?token=" + token;
        String message = messages.getMessage(event.getMessagePrefix() + ".text", null, event.getLocale());

        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom("cardio.contact@google.com");
            email.setTo(recipientAddress);
            email.setSubject(subject);
            email.setText(message + "\n" + appUrl + confirmationUrl);
            mailSender.send(email);
        } catch (MailAuthenticationException e) {
            Logger.getGlobal().log(Level.WARNING, "Error while sending email: " + e.getMessage());
            throw EmailException.createMailAthenticationException();
        }
    }
}