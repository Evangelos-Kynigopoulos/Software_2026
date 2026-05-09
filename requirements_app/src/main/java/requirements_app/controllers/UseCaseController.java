package requirements_app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import requirements_app.domain.UseCase;
import requirements_app.services.UseCaseService;

@Controller
@RequestMapping("/projects/{projectId}/use-cases")
public class UseCaseController {

    private final UseCaseService useCaseService;

    public UseCaseController(UseCaseService useCaseService) {
        this.useCaseService = useCaseService;
    }

    @GetMapping
    public String listUseCases(@PathVariable Long projectId, Model model) {
        model.addAttribute("projectId", projectId);
        model.addAttribute("useCases", useCaseService.findUseCasesByProjectId(projectId));

        return "usecases/list";
    }

    @GetMapping("/new")
    public String newUseCaseForm(@PathVariable Long projectId, Model model) {
        model.addAttribute("projectId", projectId);
        model.addAttribute("useCase", new UseCase());

        return "usecases/new";
    }

    @PostMapping
    public String createUseCase(@PathVariable Long projectId,
                                @ModelAttribute UseCase useCase,
                                @RequestParam(name = "actorsText", required = false) String actorsText) {
        useCaseService.createUseCase(projectId, useCase, actorsText);

        return "redirect:/projects/" + projectId + "/use-cases";
    }

    @GetMapping("/{useCaseId}/edit")
    public String editUseCaseForm(@PathVariable Long projectId,
                                  @PathVariable Long useCaseId,
                                  Model model) {
        UseCase useCase = useCaseService.findUseCaseById(useCaseId);

        String actorsText = "";
        if (useCase.getActors() != null) {
            actorsText = String.join(", ",
                    useCase.getActors()
                            .stream()
                            .map(actor -> actor.getName())
                            .toList()
            );
        }

        model.addAttribute("projectId", projectId);
        model.addAttribute("useCase", useCase);
        model.addAttribute("actorsText", actorsText);

        return "usecases/edit";
    }

    @PostMapping("/{useCaseId}/edit")
    public String updateUseCase(@PathVariable Long projectId,
                                @PathVariable Long useCaseId,
                                @ModelAttribute UseCase useCase,
                                @RequestParam(name = "actorsText", required = false) String actorsText) {
        useCaseService.updateUseCase(useCaseId, useCase, actorsText);

        return "redirect:/projects/" + projectId + "/use-cases";
    }

    @PostMapping("/{useCaseId}/delete")
    public String deleteUseCase(@PathVariable Long projectId,
                                @PathVariable Long useCaseId) {
        useCaseService.deleteUseCase(useCaseId);

        return "redirect:/projects/" + projectId + "/use-cases";
    }
}