package pl.lodz.p.it.cardio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.cardio.entities.Role;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Transactional(Transactional.TxType.MANDATORY)
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Collection<Role>> findByCode(String code);
    Collection<Role> findAllByCodeIn(Collection<String> code);
}
