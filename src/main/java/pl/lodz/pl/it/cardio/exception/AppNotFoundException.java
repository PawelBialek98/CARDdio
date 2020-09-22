package pl.lodz.pl.it.cardio.exception;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.pl.it.cardio.entities.Role;
import pl.lodz.pl.it.cardio.entities.User;

public class AppNotFoundException extends AppBaseException {

    public static final String ROLE_MESSAGE_KEY = "exception.roleNotFound";
    public static final String USER_MESSAGE_KEY = "exception.userNotFound";

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

    public static AppNotFoundException createUserNotFoundException(){
        AppNotFoundException nfe = new AppNotFoundException(USER_MESSAGE_KEY);
        nfe.setObjectClass(User.class);
        return nfe;
    }
}
