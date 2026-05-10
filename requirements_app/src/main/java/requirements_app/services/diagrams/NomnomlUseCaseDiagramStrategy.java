package requirements_app.services.diagrams;

import org.springframework.stereotype.Component;
import requirements_app.domain.Actor;
import requirements_app.domain.Project;
import requirements_app.domain.UseCase;
import requirements_app.repositories.ProjectRepository;
import requirements_app.repositories.UseCaseRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class NomnomlUseCaseDiagramStrategy extends AbstractUseCaseDiagramStrategy {

    private final Set<String> actorNames = new LinkedHashSet<>();

    public NomnomlUseCaseDiagramStrategy(ProjectRepository projectRepository,
                                         UseCaseRepository useCaseRepository) {
        super(projectRepository, useCaseRepository);
    }

    @Override
    public DiagramTool getTool() {
        return DiagramTool.NOMNOML;
    }

    @Override
    protected void generateHeader(StringBuilder script, Project project) {
        actorNames.clear();

        script.append("#title: Use Case Diagram - ")
                .append(escapeNomnomlText(project.getName()))
                .append("\n");

        script.append("#direction: right\n");
        script.append("#.usecase: visual=ellipse\n\n");
    }

    @Override
    protected void generateActors(StringBuilder script, List<UseCase> useCases) {
        for (UseCase useCase : useCases) {
            if (useCase.getActors() == null) {
                continue;
            }

            for (Actor actor : useCase.getActors()) {
                if (actor.getName() != null && !actor.getName().isBlank()) {
                    actorNames.add(actor.getName());
                }
            }
        }

        for (String actorName : actorNames) {
            script.append("[<actor> ")
                    .append(escapeNomnomlText(actorName))
                    .append("]\n");
        }

        script.append("\n");
    }

    @Override
    protected void generateUseCases(StringBuilder script, Project project, List<UseCase> useCases) {
        for (UseCase useCase : useCases) {
            script.append("[<usecase> ")
                    .append(escapeNomnomlText(useCase.getTitle()))
                    .append("]\n");
        }

        script.append("\n");
    }

    @Override
    protected void generateAssociations(StringBuilder script, List<UseCase> useCases) {
        for (UseCase useCase : useCases) {
            if (useCase.getActors() == null) {
                continue;
            }

            for (Actor actor : useCase.getActors()) {
                script.append("[")
                        .append(escapeNomnomlText(actor.getName()))
                        .append("] -> [")
                        .append(escapeNomnomlText(useCase.getTitle()))
                        .append("]\n");
            }
        }
    }

    @Override
    protected void generateFooter(StringBuilder script) {
        // Nomnoml does not require an explicit footer.
    }
}