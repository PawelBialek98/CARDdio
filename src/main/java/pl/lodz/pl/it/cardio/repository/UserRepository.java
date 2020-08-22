package pl.lodz.pl.it.cardio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.pl.it.cardio.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
