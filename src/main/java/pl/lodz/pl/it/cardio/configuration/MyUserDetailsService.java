package pl.lodz.pl.it.cardio.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.lodz.pl.it.cardio.entities.Role;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service("userDetailsService")
@Transactional
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private HttpServletRequest request;

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + email));

        if (user.getLocked()) {
            throw new RuntimeException("blocked");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getActivated(),
                true,
                true,
                !user.getLocked(),
                getAuthorities(user.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {
        List<GrantedAuthority> authorities
                = new ArrayList<>();
        for (Role role: roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));
            /*role.getPrivileges().stream()
                    .map(p -> new SimpleGrantedAuthority(p.getName()))
                    .forEach(authorities::add);*/
        }

        return authorities;
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
