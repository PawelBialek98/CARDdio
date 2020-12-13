package pl.lodz.p.it.cardio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.cardio.entities.User;
import pl.lodz.p.it.cardio.entities.VerificationToken;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    VerificationToken findByUser(User user);
}