package requirements_app.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import requirements_app.domain.Actor;
import requirements_app.domain.Project;
import requirements_app.domain.UseCase;
import requirements_app.repositories.ActorRepository;
import requirements_app.repositories.ProjectRepository;
import requirements_app.repositories.UseCaseRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UseCaseService {

    private final UseCaseRepository useCaseRepository;
    private final ProjectRepository projectRepository;
    private final ActorRepository actorRepository;

    public UseCaseService(UseCaseRepository useCaseRepository,
                          ProjectRepository projectRepository,
                          ActorRepository actorRepository) {
        this.useCaseRepository = useCaseRepository;
        this.projectRepository = projectRepository;
        this.actorRepository = actorRepository;
    }

    public List<UseCase> findUseCasesByProjectId(Long projectId) {
        return useCaseRepository.findByProjectId(projectId);
    }

    public UseCase findUseCaseById(Long useCaseId) {
        return useCaseRepository.findById(useCaseId)
                .orElseThrow(() -> new RuntimeException("Use case not found: " + useCaseId));
    }

    @Transactional
    public UseCase createUseCase(Long projectId, UseCase useCase, String actorsText) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        useCase.setProject(project);

        List<Actor> actors = findOrCreateActors(project, actorsText);
        useCase.setActors(actors);

        return useCaseRepository.save(useCase);
    }

    @Transactional
    public UseCase updateUseCase(Long useCaseId, UseCase updatedUseCase, String actorsText) {
        UseCase existingUseCase = findUseCaseById(useCaseId);

        existingUseCase.setTitle(updatedUseCase.getTitle());
        existingUseCase.setPreconditions(updatedUseCase.getPreconditions());
        existingUseCase.setMainFlow(updatedUseCase.getMainFlow());
        existingUseCase.setPostconditions(updatedUseCase.getPostconditions());

        Project project = existingUseCase.getProject();

        List<Actor> oldActors = new ArrayList<>();
        if (existingUseCase.getActors() != null) {
            oldActors.addAll(existingUseCase.getActors());
        }

        List<Actor> newActors = findOrCreateActors(project, actorsText);
        existingUseCase.setActors(newActors);

        UseCase savedUseCase = useCaseRepository.save(existingUseCase);
        useCaseRepository.flush();

        deleteUnusedActors(oldActors);

        return savedUseCase;
    }

    @Transactional
    public void deleteUseCase(Long useCaseId) {
        UseCase useCase = findUseCaseById(useCaseId);

        List<Actor> actorsToCheck = new ArrayList<>();
        if (useCase.getActors() != null) {
            actorsToCheck.addAll(useCase.getActors());
            useCase.getActors().clear();
        }

        useCaseRepository.delete(useCase);
        useCaseRepository.flush();

        deleteUnusedActors(actorsToCheck);
    }

    private List<Actor> findOrCreateActors(Project project, String actorsText) {
        List<Actor> actors = new ArrayList<>();

        if (actorsText == null || actorsText.trim().isEmpty()) {
            return actors;
        }

        String[] actorNames = actorsText.split(",");

        for (String rawName : actorNames) {
            String actorName = rawName.trim();

            if (actorName.isEmpty()) {
                continue;
            }

            Actor actor = actorRepository
                    .findByProjectIdAndNameIgnoreCase(project.getId(), actorName)
                    .orElseGet(() -> {
                        Actor newActor = new Actor();
                        newActor.setName(actorName);
                        newActor.setProject(project);
                        return actorRepository.save(newActor);
                    });

            if (!actors.contains(actor)) {
                actors.add(actor);
            }
        }

        return actors;
    }

    private void deleteUnusedActors(List<Actor> actorsToCheck) {
        for (Actor actor : actorsToCheck) {
            long remainingRelationships = useCaseRepository.countByActorsId(actor.getId());

            if (remainingRelationships == 0) {
                actorRepository.delete(actor);
            }
        }
    }
}