package pl.lodz.p.it.cardio.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.cardio.dto.UserDto.UserDto;
import pl.lodz.p.it.cardio.entities.VerificationToken;
import pl.lodz.p.it.cardio.entities.WorkOrder;
import pl.lodz.p.it.cardio.exception.AppNotFoundException;
import pl.lodz.p.it.cardio.exception.EmailException;
import pl.lodz.p.it.cardio.repositories.UserRepository;
import pl.lodz.p.it.cardio.repositories.VerificationTokenRepository;
import pl.lodz.p.it.cardio.service.UserService;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class CustomMailSender {

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    @Qualifier("messageSource")
    private final MessageSource messages;
    private final JavaMailSender mailSender;

    @Value("${url}")
    private String appUrl;

    public void accountOperation(UserDto user, Locale locale, String messagePrefix) throws EmailException {
        String token = UUID.randomUUID().toString();
        try {
            createVerificationToken(user, token, messagePrefix);
        } catch (AppNotFoundException e) {
            Logger.getGlobal().log(Level.INFO, e.getMessage());
        }

        String recipientAddress = user.getEmail();
        String subject = messages.getMessage(messagePrefix + ".title", null, locale);
        String confirmationUrl = "/" +
                messages.getMessage(messagePrefix + ".link", null, locale)
                + "?token=" + token;
        String message = messages.getMessage(messagePrefix + ".text", null, locale);


        SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom("cardio.contact@google.com");
            email.setTo(recipientAddress);
            email.setSubject(subject);
            email.setText(message + "\n" + this.appUrl + confirmationUrl);
            mailSender.send(email);


    }

    public void orderStatusChange(WorkOrder workOrder, Locale locale, String messagePrefix) throws EmailException {
        ResourceBundle emailResourceBundle = ResourceBundle.getBundle("i18n/email", locale);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n/messages", locale);

        String recipientAddress = workOrder.getCustomer().getEmail();
        String subject = emailResourceBundle.getString(messagePrefix.concat(".title"));
        String wotCode = workOrder.getWorkOrderType().getCode();
        try {
            wotCode = resourceBundle.getString(wotCode);
        } catch (MissingResourceException e) {
            Logger.getGlobal().log(Level.INFO, "Not found resource for key: " + wotCode);
            wotCode = workOrder.getWorkOrderType().getName();
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String pattern = emailResourceBundle.getString(messagePrefix.concat(".text"));
        String message = MessageFormat.format(pattern,
                resourceBundle.getString(workOrder.getCurrentStatus().getCode()),
                dateFormat.format(workOrder.getStartDateTime()),
                wotCode,
                workOrder.getWorkOrderType().getRequiredTime() + " min");

        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom("cardio.contact@google.com");
            email.setTo(recipientAddress);
            email.setSubject(subject);
            email.setText(message);
            mailSender.send(email);
        } catch (MailAuthenticationException e) {
            Logger.getGlobal().log(Level.WARNING, "Error while sending email: " + e.getMessage());
            throw EmailException.createMailAthenticationException();
        }
    }

    private void createVerificationToken(UserDto userDto, String token, String type) throws AppNotFoundException {
        VerificationToken myToken = new VerificationToken(token,
                userRepository.findByEmail(userDto.getEmail()).orElseThrow(AppNotFoundException::createUserNotFoundException),
                type);
        tokenRepository.save(myToken);
    }
}
