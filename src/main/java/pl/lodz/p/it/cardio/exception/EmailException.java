package pl.lodz.p.it.cardio.exception;

public class EmailException extends AppBaseException {

    public static final String MAIL_AUTHENTICATION_EXCEPTION = "exception.mailAuthenticationException";

    public EmailException(String message) {
        super(message);
    }

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public static EmailException createMailAthenticationException() {
        return new EmailException(resourceBundle.getString(MAIL_AUTHENTICATION_EXCEPTION));
    }
}
