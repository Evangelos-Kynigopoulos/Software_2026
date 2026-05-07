package requirements_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import requirements_app.domain.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    // Basic CRUD operations (save, findById, delete, findAll) are inherited automatically
}