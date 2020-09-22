package pl.lodz.pl.it.cardio.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pl.lodz.pl.it.cardio.dto.UserDto;
import pl.lodz.pl.it.cardio.service.UserService;

import java.util.UUID;

@Component
public class ResetPasswordListener implements
        ApplicationListener<OnResetPasswordCompleteEvent> {

    private final UserService service;

    @Qualifier("messageSource")
    private final MessageSource messages;

    private final JavaMailSender mailSender;

    @Autowired
    public ResetPasswordListener(UserService userService, JavaMailSender javaMailSender, MessageSource messages) {
        this.service = userService;
        this.mailSender = javaMailSender;
        this.messages = messages;
    }

    @Override
    public void onApplicationEvent(OnResetPasswordCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnResetPasswordCompleteEvent event) {
        UserDto user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Reset Password";
        String confirmationUrl
                = event.getAppUrl() + "/confirmResetPassword?token=" + token;
        //String message = messages.getMessage("message.regSucc", null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("cardio.contact@google.com");
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("http://localhost:8080" + confirmationUrl);
        mailSender.send(email);
    }
}