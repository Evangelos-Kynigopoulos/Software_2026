package requirements_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import requirements_app.domain.CRCCard;
import java.util.List;

@Repository
public interface CRCCardRepository extends JpaRepository<CRCCard, Long> {
    List<CRCCard> findByProjectId(Long projectId);
}