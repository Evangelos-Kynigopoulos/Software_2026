package requirements_app.services;

import org.springframework.stereotype.Service;
import requirements_app.services.diagrams.DiagramGenerationStrategy;
import requirements_app.services.diagrams.DiagramGeneratorFactory;
import requirements_app.services.diagrams.DiagramKind;
import requirements_app.services.diagrams.DiagramTool;

@Service
public class DiagramService {

    private final DiagramGeneratorFactory diagramGeneratorFactory;

    public DiagramService(DiagramGeneratorFactory diagramGeneratorFactory) {
        this.diagramGeneratorFactory = diagramGeneratorFactory;
    }

    public String generateUseCaseDiagram(Long projectId, DiagramTool diagramTool) {
        DiagramGenerationStrategy strategy = diagramGeneratorFactory.getStrategy(
                diagramTool,
                DiagramKind.USE_CASE
        );

        return strategy.generate(projectId);
    }

    public String generateCRCDiagram(Long projectId, DiagramTool diagramTool) {
        DiagramGenerationStrategy strategy = diagramGeneratorFactory.getStrategy(
                diagramTool,
                DiagramKind.CRC
        );

        return strategy.generate(projectId);
    }
}