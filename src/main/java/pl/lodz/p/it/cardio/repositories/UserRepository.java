package pl.lodz.p.it.cardio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.cardio.entities.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional(Transactional.TxType.MANDATORY)
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndLockedIsFalse(String email);
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<User> findByBusinessKey(UUID userBusinessKey);
    int countAllByActivatedIsTrueAndLockedIsFalse();
    List<User> findAllByActivatedIsFalse();
}
