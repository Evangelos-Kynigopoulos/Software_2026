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
public class PlantUMLCRCDiagramStrategy extends AbstractCRCDiagramStrategy {

    private final Map<Long, String> crcAliasesById = new LinkedHashMap<>();
    private final Map<String, String> crcAliasesByClassName = new LinkedHashMap<>();

    public PlantUMLCRCDiagramStrategy(ProjectRepository projectRepository,
                                      CRCCardRepository crcCardRepository) {
        super(projectRepository, crcCardRepository);
    }

    @Override
    public DiagramTool getTool() {
        return DiagramTool.PLANTUML;
    }

    @Override
    protected void generateHeader(StringBuilder script, Project project) {
        crcAliasesById.clear();
        crcAliasesByClassName.clear();

        script.append("@startuml\n");
        script.append("title CRC/Class Diagram - ")
                .append(escapePlantUmlText(project.getName()))
                .append("\n\n");

        script.append("skinparam classAttributeIconSize 0\n\n");
    }

    @Override
    protected void generateClasses(StringBuilder script, List<CRCCard> crcCards) {
        int crcCounter = 1;

        for (CRCCard crcCard : crcCards) {
            String alias = "C" + crcCounter++;

            crcAliasesById.put(crcCard.getId(), alias);

            if (crcCard.getClassName() != null) {
                crcAliasesByClassName.put(normalizeName(crcCard.getClassName()), alias);
            }

            script.append("class \"")
                    .append(escapePlantUmlText(crcCard.getClassName()))
                    .append("\" as ")
                    .append(alias)
                    .append("\n");
        }

        script.append("\n");

        for (CRCCard crcCard : crcCards) {
            String alias = crcAliasesById.get(crcCard.getId());

            script.append("note right of ")
                    .append(alias)
                    .append("\n");

            script.append("Responsibilities:\n");
            script.append(formatPlantUmlNoteText(crcCard.getResponsibilities()));

            script.append("\nCollaborations:\n");
            script.append(formatPlantUmlNoteText(crcCard.getCollaborations()));

            script.append("end note\n\n");
        }
    }

    @Override
    protected void generateAssociations(StringBuilder script, List<CRCCard> crcCards) {
        for (CRCCard crcCard : crcCards) {
            String sourceAlias = crcAliasesById.get(crcCard.getId());

            if (crcCard.getCollaborations() == null || crcCard.getCollaborations().isBlank()) {
                continue;
            }

            String[] collaborationNames = crcCard.getCollaborations().split("[,;\\n\\r]+");

            for (String collaborationName : collaborationNames) {
                String cleanedName = collaborationName.trim();

                if (cleanedName.isEmpty()) {
                    continue;
                }

                String targetAlias = crcAliasesByClassName.get(normalizeName(cleanedName));

                if (targetAlias != null && !targetAlias.equals(sourceAlias)) {
                    script.append(sourceAlias)
                            .append(" --> ")
                            .append(targetAlias)
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