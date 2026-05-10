package requirements_app.services.diagrams;

public enum DiagramTool {
    PLANTUML,
    NOMNOML;

    public static DiagramTool fromString(String value) {
        if (value == null || value.isBlank()) {
            return PLANTUML;
        }

        return switch (value.trim().toLowerCase()) {
            case "plantuml" -> PLANTUML;
            case "nomnoml" -> NOMNOML;
            default -> PLANTUML;
        };
    }
}