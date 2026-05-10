package requirements_app.services.diagrams;

import requirements_app.domain.Project;
import requirements_app.domain.UseCase;
import requirements_app.repositories.ProjectRepository;
import requirements_app.repositories.UseCaseRepository;

import java.util.List;

public abstract class AbstractUseCaseDiagramStrategy implements DiagramGenerationStrategy {

    protected final ProjectRepository projectRepository;
    protected final UseCaseRepository useCaseRepository;

    protected AbstractUseCaseDiagramStrategy(ProjectRepository projectRepository,
                                             UseCaseRepository useCaseRepository) {
        this.projectRepository = projectRepository;
        this.useCaseRepository = useCaseRepository;
    }

    @Override
    public final DiagramKind getKind() {
        return DiagramKind.USE_CASE;
    }

    @Override
    public final String generate(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        List<UseCase> useCases = useCaseRepository.findByProjectId(projectId);

        StringBuilder script = new StringBuilder();

        generateHeader(script, project);
        generateActors(script, useCases);
        generateUseCases(script, project, useCases);
        generateAssociations(script, useCases);
        generateFooter(script);

        return script.toString();
    }

    protected abstract void generateHeader(StringBuilder script, Project project);

    protected abstract void generateActors(StringBuilder script, List<UseCase> useCases);

    protected abstract void generateUseCases(StringBuilder script, Project project, List<UseCase> useCases);

    protected abstract void generateAssociations(StringBuilder script, List<UseCase> useCases);

    protected abstract void generateFooter(StringBuilder script);

    protected String escapePlantUmlText(String text) {
        if (text == null) {
            return "";
        }

        return text.replace("\"", "\\\"");
    }

    protected String escapeNomnomlText(String text) {
        if (text == null) {
            return "";
        }

        return text
                .replace("[", "(")
                .replace("]", ")")
                .replace("|", "/")
                .replace("\r", " ")
                .replace("\n", " ")
                .trim();
    }
}