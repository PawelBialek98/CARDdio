package pl.lodz.pl.it.cardio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.pl.it.cardio.entities.Status;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByCode(String code);
}
