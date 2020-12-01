package pl.lodz.pl.it.cardio.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.LocaleResolver;
import pl.lodz.pl.it.cardio.entities.User;

import java.util.ResourceBundle;

public class ValueNotUniqueException extends AppBaseException {

    public static final String EMAIL_MESSAGE_KEY = "exception.emailNotUnique";

    @Getter
    @Setter
    private Object object;

    public ValueNotUniqueException(String message) {
        super(message);
    }

    public static ValueNotUniqueException createEmailNotUniqueException(User user) {
        ValueNotUniqueException nue = new ValueNotUniqueException(resourceBundle.getString(EMAIL_MESSAGE_KEY));
        nue.setObject(user);
        return nue;
    }
}
