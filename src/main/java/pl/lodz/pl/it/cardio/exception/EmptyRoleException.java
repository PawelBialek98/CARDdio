package pl.lodz.pl.it.cardio.exception;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.pl.it.cardio.entities.User;

public class EmptyRoleException extends AppBaseException{

    public static final String EMPTY_ROLE_MESSAGE_KEY = "exception.emptyRole";

    @Getter
    @Setter
    private Class objectClass;

    public EmptyRoleException(String message) {
        super(message);
    }

    public EmptyRoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public static EmptyRoleException createEmptyRoleException(){
        EmptyRoleException ere = new EmptyRoleException(resourceBundle.getString(EMPTY_ROLE_MESSAGE_KEY));
        ere.setObjectClass(User.class);
        return ere;
    }
}
