package requirements_app.services.diagrams;

import org.springframework.stereotype.Component;
import requirements_app.domain.CRCCard;
import requirements_app.domain.Project;
import requirements_app.repositories.CRCCardRepository;
import requirements_app.repositories.ProjectRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class NomnomlCRCDiagramStrategy extends AbstractCRCDiagramStrategy {

    private final Map<String, String> classNames = new LinkedHashMap<>();

    public NomnomlCRCDiagramStrategy(ProjectRepository projectRepository,
                                     CRCCardRepository crcCardRepository) {
        super(projectRepository, crcCardRepository);
    }

    @Override
    public DiagramTool getTool() {
        return DiagramTool.NOMNOML;
    }

    @Override
    protected void generateHeader(StringBuilder script, Project project) {
        classNames.clear();

        script.append("#title: CRC/Class Diagram - ")
                .append(escapeNomnomlText(project.getName()))
                .append("\n");

        script.append("#direction: right\n\n");
    }

    @Override
    protected void generateClasses(StringBuilder script, List<CRCCard> crcCards) {
        for (CRCCard crcCard : crcCards) {
            String className = escapeNomnomlText(crcCard.getClassName());

            classNames.put(normalizeName(crcCard.getClassName()), className);

            script.append("[")
                    .append(className)
                    .append("|Responsibilities:; ")
                    .append(formatNomnomlCompartmentText(crcCard.getResponsibilities()))
                    .append("|Collaborations:; ")
                    .append(formatNomnomlCompartmentText(crcCard.getCollaborations()))
                    .append("]\n");
        }

        script.append("\n");
    }

    @Override
    protected void generateAssociations(StringBuilder script, List<CRCCard> crcCards) {
        for (CRCCard crcCard : crcCards) {
            if (crcCard.getCollaborations() == null || crcCard.getCollaborations().isBlank()) {
                continue;
            }

            String sourceClassName = classNames.get(normalizeName(crcCard.getClassName()));
            String[] collaborationNames = crcCard.getCollaborations().split("[,;\\n\\r]+");

            for (String collaborationName : collaborationNames) {
                String cleanedName = collaborationName.trim();

                if (cleanedName.isEmpty()) {
                    continue;
                }

                String targetClassName = classNames.get(normalizeName(cleanedName));

                if (targetClassName != null && !targetClassName.equals(sourceClassName)) {
                    script.append("[")
                            .append(sourceClassName)
                            .append("] -> [")
                            .append(targetClassName)
                            .append("]\n");
                }
            }
        }
    }

    @Override
    protected void generateFooter(StringBuilder script) {
        // Nomnoml does not require an explicit footer.
    }
}