package requirements_app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import requirements_app.services.DiagramService;
import requirements_app.services.diagrams.DiagramTool;

@Controller
@RequestMapping("/projects/{projectId}/diagrams")
public class DiagramController {

    private final DiagramService diagramService;

    public DiagramController(DiagramService diagramService) {
        this.diagramService = diagramService;
    }

    @GetMapping("/use-case")
    public String generateUseCaseDiagram(@PathVariable Long projectId,
                                         @RequestParam(defaultValue = "plantuml") String tool,
                                         Model model) {
        DiagramTool diagramTool = DiagramTool.fromString(tool);
        String diagramScript = diagramService.generateUseCaseDiagram(projectId, diagramTool);

        model.addAttribute("projectId", projectId);
        model.addAttribute("diagramScript", diagramScript);
        model.addAttribute("selectedTool", diagramTool.name().toLowerCase());

        return "diagrams/usecase";
    }

    @GetMapping("/crc")
    public String generateCRCDiagram(@PathVariable Long projectId,
                                     @RequestParam(defaultValue = "plantuml") String tool,
                                     Model model) {
        DiagramTool diagramTool = DiagramTool.fromString(tool);
        String diagramScript = diagramService.generateCRCDiagram(projectId, diagramTool);

        model.addAttribute("projectId", projectId);
        model.addAttribute("diagramScript", diagramScript);
        model.addAttribute("selectedTool", diagramTool.name().toLowerCase());

        return "diagrams/crc";
    }
}