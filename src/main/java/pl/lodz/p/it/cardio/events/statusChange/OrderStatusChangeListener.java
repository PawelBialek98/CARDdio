package pl.lodz.p.it.cardio.events.statusChange;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class OrderStatusChangeListener implements ApplicationListener<OrderStatusChangeEvent> {

    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OrderStatusChangeEvent orderStatusChangeEvent) {
        ResourceBundle emailResourceBundle = ResourceBundle.getBundle("i18n/email",orderStatusChangeEvent.getLocale());
        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n/messages", orderStatusChangeEvent.getLocale());

        String recipientAddress = orderStatusChangeEvent.getWorkOrder().getCustomer().getEmail();
        String subject = emailResourceBundle.getString(orderStatusChangeEvent.getMessagePrefix().concat(".title"));
        String wotCode = orderStatusChangeEvent.getWorkOrder().getWorkOrderType().getCode();
        try{
            wotCode = resourceBundle.getString(wotCode);
        } catch (MissingResourceException e){
            Logger.getGlobal().log(Level.INFO, "Not found resource for key: " + wotCode);
            wotCode = orderStatusChangeEvent.getWorkOrder().getWorkOrderType().getName();
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String pattern = emailResourceBundle.getString(orderStatusChangeEvent.getMessagePrefix().concat(".text"));
        String message = MessageFormat.format(pattern,
                resourceBundle.getString(orderStatusChangeEvent.getWorkOrder().getCurrentStatus().getCode()),
                dateFormat.format(orderStatusChangeEvent.getWorkOrder().getStartDateTime()),
                wotCode,
                orderStatusChangeEvent.getWorkOrder().getWorkOrderType().getRequiredTime() + " min");

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("cardio.contact@google.com");
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }
}
