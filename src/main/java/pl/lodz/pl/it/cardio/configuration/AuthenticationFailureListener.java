package pl.lodz.pl.it.cardio.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener {

    private final HttpServletRequest request;
    private final UserRepository userRepository;

    @EventListener
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {

        String email = e.getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + email));

        Logger.getGlobal().log(Level.WARNING, "Someone trys to log in to account: " + email + " from IP adress: " + getClientIP());

        user.setInvalidLoginAttempts(user.getInvalidLoginAttempts() + 1);
        if(user.getInvalidLoginAttempts() >= 3 && !user.getLocked()){
            user.setLocked(true);
        }

        userRepository.save(user);
    }

    @EventListener
    public void onApplicationEvent(AuthenticationFailureLockedEvent e){
        String email = e.getAuthentication().getName();

        Logger.getGlobal().log(Level.WARNING, "Someone tries to log in to blocked account: " + email + " from IP adress: " + getClientIP());
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
