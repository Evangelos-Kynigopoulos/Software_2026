package requirements_app.services.diagrams;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiagramGeneratorFactory {

    private final List<DiagramGenerationStrategy> strategies;

    public DiagramGeneratorFactory(List<DiagramGenerationStrategy> strategies) {
        this.strategies = strategies;
    }

    public DiagramGenerationStrategy getStrategy(DiagramTool tool, DiagramKind kind) {
        return strategies.stream()
                .filter(strategy -> strategy.getTool() == tool)
                .filter(strategy -> strategy.getKind() == kind)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "No diagram generation strategy found for tool " + tool + " and kind " + kind
                ));
    }
}