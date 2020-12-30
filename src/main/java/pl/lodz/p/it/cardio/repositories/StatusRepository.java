package pl.lodz.p.it.cardio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.cardio.entities.Status;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional(Transactional.TxType.MANDATORY)
public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByCode(String code);
}
