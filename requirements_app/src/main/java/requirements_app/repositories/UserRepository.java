package requirements_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import requirements_app.domain.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // Spring Data JPA automatically writes the SQL for this based on the method name
    Optional<User> findByUsername(String username);
}
