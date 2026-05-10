package requirements_app.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import requirements_app.domain.CRCCard;
import requirements_app.domain.Project;
import requirements_app.domain.UseCase;
import requirements_app.repositories.CRCCardRepository;
import requirements_app.repositories.ProjectRepository;
import requirements_app.repositories.UseCaseRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CRCCardService {

    private final CRCCardRepository crcCardRepository;
    private final ProjectRepository projectRepository;
    private final UseCaseRepository useCaseRepository;

    public CRCCardService(CRCCardRepository crcCardRepository,
                          ProjectRepository projectRepository,
                          UseCaseRepository useCaseRepository) {
        this.crcCardRepository = crcCardRepository;
        this.projectRepository = projectRepository;
        this.useCaseRepository = useCaseRepository;
    }

    public List<CRCCard> findCRCCardsByProjectId(Long projectId) {
        return crcCardRepository.findByProjectId(projectId);
    }

    public CRCCard findById(Long id) {
        return crcCardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CRC Card not found with id: " + id));
    }

    @Transactional
    public void createCRCCard(Long projectId, CRCCard crcCard, List<Long> useCaseIds) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        crcCard.setProject(project);

        CRCCard savedCRCCard = crcCardRepository.save(crcCard);

        linkCRCCardToUseCases(savedCRCCard, useCaseIds);
    }

    @Transactional
    public void updateCRCCard(Long id, CRCCard updatedCRCCard, List<Long> useCaseIds) {
        CRCCard existingCRCCard = findById(id);

        existingCRCCard.setClassName(updatedCRCCard.getClassName());
        existingCRCCard.setResponsibilities(updatedCRCCard.getResponsibilities());
        existingCRCCard.setCollaborations(updatedCRCCard.getCollaborations());

        CRCCard savedCRCCard = crcCardRepository.save(existingCRCCard);

        unlinkCRCCardFromAllUseCases(savedCRCCard);
        linkCRCCardToUseCases(savedCRCCard, useCaseIds);
    }

    @Transactional
    public void deleteCRCCard(Long id) {
        CRCCard crcCard = findById(id);

        unlinkCRCCardFromAllUseCases(crcCard);

        crcCardRepository.delete(crcCard);
    }

    private void linkCRCCardToUseCases(CRCCard crcCard, List<Long> useCaseIds) {
        if (useCaseIds == null || useCaseIds.isEmpty()) {
            return;
        }

        List<UseCase> selectedUseCases = useCaseRepository.findAllById(useCaseIds);

        for (UseCase useCase : selectedUseCases) {
            if (useCase.getCrcCards() == null) {
                useCase.setCrcCards(new ArrayList<>());
            }

            if (!useCase.getCrcCards().contains(crcCard)) {
                useCase.getCrcCards().add(crcCard);
            }
        }

        useCaseRepository.saveAll(selectedUseCases);
    }

    private void unlinkCRCCardFromAllUseCases(CRCCard crcCard) {
        List<UseCase> linkedUseCases = new ArrayList<>(crcCard.getUseCases());

        for (UseCase useCase : linkedUseCases) {
            useCase.getCrcCards().remove(crcCard);
        }

        useCaseRepository.saveAll(linkedUseCases);
    }
}