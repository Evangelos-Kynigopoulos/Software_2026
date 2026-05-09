package requirements_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import requirements_app.domain.UseCase;

import java.util.List;

@Repository
public interface UseCaseRepository extends JpaRepository<UseCase, Long> {

    List<UseCase> findByProjectId(Long projectId);

    long countByActorsId(Long actorId); //used to delete actors with no relationships
}