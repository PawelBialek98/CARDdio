package pl.lodz.pl.it.cardio.exception;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.pl.it.cardio.entities.Role;

public class AppNotFoundException extends AppBaseException {

    public static final String ROLE_MESSAGE_KEY = "exception.roleNotFound";

    @Getter
    @Setter
    private Class objectClass;

    public AppNotFoundException(String message) {
        super(message);
    }

    public AppNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static AppNotFoundException createRoleNotFoundException(){
        AppNotFoundException nfe = new AppNotFoundException(ROLE_MESSAGE_KEY);
        nfe.setObjectClass(Role.class);
        return nfe;
    }
}
