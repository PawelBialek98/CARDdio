package pl.lodz.pl.it.cardio.exception;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.pl.it.cardio.entities.Role;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.entities.VerificationToken;
import pl.lodz.pl.it.cardio.entities.WorkOrder;

public class AppNotFoundException extends AppBaseException {

    public static final String ROLE_MESSAGE_KEY = "exception.roleNotFound";
    public static final String USER_MESSAGE_KEY = "exception.userNotFound";
    public static final String WORK_ORDER_MESSAGE_KEY = "exception.workOrderNotFound";
    public static final String TOKEN_MESSAGE_KEY = "exception.tokenNotFound";

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
        AppNotFoundException nfe = new AppNotFoundException(resourceBundle.getString(ROLE_MESSAGE_KEY));
        nfe.setObjectClass(Role.class);
        return nfe;
    }

    public static AppNotFoundException createUserNotFoundException(){
        AppNotFoundException nfe = new AppNotFoundException(resourceBundle.getString(USER_MESSAGE_KEY));
        nfe.setObjectClass(User.class);
        return nfe;
    }

    public static AppNotFoundException createWorkOrderNotFoundException(){
        AppNotFoundException nfe = new AppNotFoundException(resourceBundle.getString(WORK_ORDER_MESSAGE_KEY));
        nfe.setObjectClass(WorkOrder.class);
        return nfe;
    }

    public static AppNotFoundException createTokenNotFoundException(){
        AppNotFoundException nfe = new AppNotFoundException(resourceBundle.getString(TOKEN_MESSAGE_KEY));
        nfe.setObjectClass(VerificationToken.class);
        return nfe;
    }
}
