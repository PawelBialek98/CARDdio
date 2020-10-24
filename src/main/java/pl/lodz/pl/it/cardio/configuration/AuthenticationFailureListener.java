package pl.lodz.pl.it.cardio.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.repositories.UserRepository;
import pl.lodz.pl.it.cardio.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final HttpServletRequest request;
   // private final UserRepository userRepository;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        WebAuthenticationDetails auth = (WebAuthenticationDetails)
                e.getAuthentication().getDetails();

        String email = e.getAuthentication().getName();

//        User user = userRepository.findByEmail(email)
       //         .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + email));

        Logger.getGlobal().log(Level.WARNING, "Someone trys to log in to account: " + email + " from IP adress: " + getClientIP());

        //user.setInvalidLoginAttempts(user.getInvalidLoginAttempts() + 1);
        //if(user.getInvalidLoginAttempts() == 3){
        //    user.setLocked(true);
        //}

        //userRepository.save(user);
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
