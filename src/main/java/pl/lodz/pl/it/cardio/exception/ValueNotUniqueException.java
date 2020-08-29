package pl.lodz.pl.it.cardio.exception;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.pl.it.cardio.entities.User;

public class ValueNotUniqueException extends AppBaseException {

    public static final String EMAIL_MESSAGE_KEY = "exception.emailNotUnique";

    @Getter
    @Setter
    private Object object;

    public ValueNotUniqueException(String message) {
        super(message);
    }

    public static ValueNotUniqueException createEmailNotUniqueException(User user) {
        ValueNotUniqueException nue = new ValueNotUniqueException(EMAIL_MESSAGE_KEY);
        nue.setObject(user);
        return nue;
    }
}
