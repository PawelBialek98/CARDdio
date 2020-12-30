package pl.lodz.p.it.cardio.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.cardio.repositories.UserRepository;
import pl.lodz.p.it.cardio.entities.User;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.logging.Level;
import java.util.logging.Logger;


@Component
@Transactional(Transactional.TxType.REQUIRES_NEW)
@RequiredArgsConstructor
public class AuthenticationSuccessHandler implements ApplicationListener<AuthenticationSuccessEvent> {

    private final HttpServletRequest request;
    private final UserRepository userRepository;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent e) {

        String email = e.getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + email));

        Logger.getGlobal().log(Level.WARNING, "Successfull login into: " + email + " from IP adress: " + getClientIP());

        user.setInvalidLoginAttempts(0);

        userRepository.save(user);
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
