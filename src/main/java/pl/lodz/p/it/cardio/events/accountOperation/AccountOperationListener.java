package pl.lodz.p.it.cardio.events.accountOperation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.cardio.dto.UserDto.UserDto;
import pl.lodz.p.it.cardio.service.UserService;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountOperationListener implements
        ApplicationListener<AccountOperationEvent> {

    private final UserService service;

    @Qualifier("messageSource")
    private final MessageSource messages;

    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(AccountOperationEvent event) {
        UserDto user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token, event.getMessagePrefix());

        String recipientAddress = user.getEmail();
        String subject = messages.getMessage(event.getMessagePrefix() + ".title", null, event.getLocale());
        String confirmationUrl
                = event.getAppUrl() + "/" +
                messages.getMessage(event.getMessagePrefix() + ".link", null, event.getLocale())
                + "?token=" + token;
        String message = messages.getMessage(event.getMessagePrefix() + ".text", null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("cardio.contact@google.com");
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\n" + "http://localhost:8080" + confirmationUrl);
        mailSender.send(email);
    }
}