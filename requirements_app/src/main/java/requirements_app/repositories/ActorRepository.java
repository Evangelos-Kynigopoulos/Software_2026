package requirements_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import requirements_app.domain.Actor;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

    List<Actor> findByProjectId(Long projectId);

    Optional<Actor> findByProjectIdAndNameIgnoreCase(Long projectId, String name);
}