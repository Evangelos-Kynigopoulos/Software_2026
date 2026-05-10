package requirements_app.services.diagrams;

import org.springframework.stereotype.Component;
import requirements_app.domain.Actor;
import requirements_app.domain.Project;
import requirements_app.domain.UseCase;
import requirements_app.repositories.ProjectRepository;
import requirements_app.repositories.UseCaseRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class PlantUMLUseCaseDiagramStrategy extends AbstractUseCaseDiagramStrategy {

    private final Map<Long, String> actorAliases = new LinkedHashMap<>();
    private final Map<Long, String> useCaseAliases = new LinkedHashMap<>();

    public PlantUMLUseCaseDiagramStrategy(ProjectRepository projectRepository,
                                          UseCaseRepository useCaseRepository) {
        super(projectRepository, useCaseRepository);
    }

    @Override
    public DiagramTool getTool() {
        return DiagramTool.PLANTUML;
    }

    @Override
    protected void generateHeader(StringBuilder script, Project project) {
        actorAliases.clear();
        useCaseAliases.clear();

        script.append("@startuml\n");
        script.append("left to right direction\n\n");
    }

    @Override
    protected void generateActors(StringBuilder script, List<UseCase> useCases) {
        int actorCounter = 1;

        for (UseCase useCase : useCases) {
            if (useCase.getActors() == null) {
                continue;
            }

            for (Actor actor : useCase.getActors()) {
                if (!actorAliases.containsKey(actor.getId())) {
                    String alias = "A" + actorCounter++;
                    actorAliases.put(actor.getId(), alias);

                    script.append("actor \"")
                            .append(escapePlantUmlText(actor.getName()))
                            .append("\" as ")
                            .append(alias)
                            .append("\n");
                }
            }
        }

        script.append("\n");
    }

    @Override
    protected void generateUseCases(StringBuilder script, Project project, List<UseCase> useCases) {
        int useCaseCounter = 1;

        script.append("rectangle \"")
                .append(escapePlantUmlText(project.getName()))
                .append("\" {\n");

        for (UseCase useCase : useCases) {
            String alias = "UC" + useCaseCounter++;
            useCaseAliases.put(useCase.getId(), alias);

            script.append("    usecase \"")
                    .append(escapePlantUmlText(useCase.getTitle()))
                    .append("\" as ")
                    .append(alias)
                    .append("\n");
        }

        script.append("}\n\n");
    }

    @Override
    protected void generateAssociations(StringBuilder script, List<UseCase> useCases) {
        for (UseCase useCase : useCases) {
            if (useCase.getActors() == null) {
                continue;
            }

            String useCaseAlias = useCaseAliases.get(useCase.getId());

            for (Actor actor : useCase.getActors()) {
                String actorAlias = actorAliases.get(actor.getId());

                if (actorAlias != null && useCaseAlias != null) {
                    script.append(actorAlias)
                            .append(" --> ")
                            .append(useCaseAlias)
                            .append("\n");
                }
            }
        }

        script.append("\n");
    }

    @Override
    protected void generateFooter(StringBuilder script) {
        script.append("@enduml");
    }
}