package requirements_app.services.diagrams;

import requirements_app.domain.CRCCard;
import requirements_app.domain.Project;
import requirements_app.repositories.CRCCardRepository;
import requirements_app.repositories.ProjectRepository;

import java.util.List;

public abstract class AbstractCRCDiagramStrategy implements DiagramGenerationStrategy {

    protected final ProjectRepository projectRepository;
    protected final CRCCardRepository crcCardRepository;

    protected AbstractCRCDiagramStrategy(ProjectRepository projectRepository,
                                         CRCCardRepository crcCardRepository) {
        this.projectRepository = projectRepository;
        this.crcCardRepository = crcCardRepository;
    }

    @Override
    public final DiagramKind getKind() {
        return DiagramKind.CRC;
    }

    @Override
    public final String generate(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        List<CRCCard> crcCards = crcCardRepository.findByProjectId(projectId);

        StringBuilder script = new StringBuilder();

        generateHeader(script, project);
        generateClasses(script, crcCards);
        generateAssociations(script, crcCards);
        generateFooter(script);

        return script.toString();
    }

    protected abstract void generateHeader(StringBuilder script, Project project);

    protected abstract void generateClasses(StringBuilder script, List<CRCCard> crcCards);

    protected abstract void generateAssociations(StringBuilder script, List<CRCCard> crcCards);

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

    protected String normalizeName(String text) {
        if (text == null) {
            return "";
        }

        return text.trim().toLowerCase();
    }

    protected String formatPlantUmlNoteText(String text) {
        if (text == null || text.isBlank()) {
            return "- None\n";
        }

        StringBuilder formattedText = new StringBuilder();
        String[] lines = text.split("\\R");

        for (String line : lines) {
            String cleanedLine = line.trim();

            if (!cleanedLine.isEmpty()) {
                formattedText.append(escapePlantUmlText(cleanedLine)).append("\n");
            }
        }

        return formattedText.toString();
    }

    protected String formatNomnomlCompartmentText(String text) {
        if (text == null || text.isBlank()) {
            return "- None";
        }

        StringBuilder formattedText = new StringBuilder();
        String[] lines = text.split("\\R");

        for (String line : lines) {
            String cleanedLine = line.trim();

            if (!cleanedLine.isEmpty()) {
                if (!formattedText.isEmpty()) {
                    formattedText.append("; ");
                }

                formattedText.append(escapeNomnomlText(cleanedLine));
            }
        }

        return formattedText.toString();
    }
}