package requirements_app.services.diagrams;

public interface DiagramGenerationStrategy {

    DiagramTool getTool();

    DiagramKind getKind();

    String generate(Long projectId);
}