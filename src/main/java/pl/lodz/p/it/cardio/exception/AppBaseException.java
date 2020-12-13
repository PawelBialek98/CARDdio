package pl.lodz.p.it.cardio.exception;

import org.apache.tomcat.util.descriptor.LocalResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.ResourceBundle;

public class AppBaseException extends Exception {

    protected static ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n/errors", LocaleContextHolder.getLocale());

    public AppBaseException(String message) {
        super(message);
    }
    public AppBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}