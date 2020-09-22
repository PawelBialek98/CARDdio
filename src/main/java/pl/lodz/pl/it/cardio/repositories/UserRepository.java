package pl.lodz.pl.it.cardio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.pl.it.cardio.entities.User;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
