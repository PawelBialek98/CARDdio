package pl.lodz.p.it.cardio.configuration;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n/errors", LocaleContextHolder.getLocale());

        String errorMessage = resourceBundle.getString("authentication.failed");

        if (exception.getMessage()
                .equalsIgnoreCase("blocked")) {
            errorMessage = resourceBundle.getString("authentication.blocked");
        }

        setDefaultFailureUrl("/login?error=true");

        super.onAuthenticationFailure(request, response, exception);

        request.getSession()
                .setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
    }
}
