package requirements_app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import requirements_app.domain.CRCCard;
import requirements_app.services.CRCCardService;
import requirements_app.services.UseCaseService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/projects/{projectId}/crc-cards")
public class CRCCardController {

    private final CRCCardService crcCardService;
    private final UseCaseService useCaseService;

    public CRCCardController(CRCCardService crcCardService,
                             UseCaseService useCaseService) {
        this.crcCardService = crcCardService;
        this.useCaseService = useCaseService;
    }

    @GetMapping
    public String listCRCCards(@PathVariable Long projectId, Model model) {
        model.addAttribute("projectId", projectId);
        model.addAttribute("crcCards", crcCardService.findCRCCardsByProjectId(projectId));

        return "crccards/list";
    }

    @GetMapping("/new")
    public String newCRCCardForm(@PathVariable Long projectId, Model model) {
        model.addAttribute("projectId", projectId);
        model.addAttribute("crcCard", new CRCCard());
        model.addAttribute("useCases", useCaseService.findUseCasesByProjectId(projectId));

        return "crccards/new";
    }

    @PostMapping
    public String createCRCCard(@PathVariable Long projectId,
                                @ModelAttribute CRCCard crcCard,
                                @RequestParam(required = false) List<Long> useCaseIds) {
        crcCardService.createCRCCard(projectId, crcCard, useCaseIds);

        return "redirect:/projects/" + projectId + "/crc-cards";
    }

    @GetMapping("/{crcCardId}/edit")
    public String editCRCCardForm(@PathVariable Long projectId,
                                  @PathVariable Long crcCardId,
                                  Model model) {
        CRCCard crcCard = crcCardService.findById(crcCardId);

        List<Long> selectedUseCaseIds = new ArrayList<>();

        if (crcCard.getUseCases() != null) {
            selectedUseCaseIds = crcCard.getUseCases()
                    .stream()
                    .map(useCase -> useCase.getId())
                    .toList();
        }

        model.addAttribute("projectId", projectId);
        model.addAttribute("crcCard", crcCard);
        model.addAttribute("useCases", useCaseService.findUseCasesByProjectId(projectId));
        model.addAttribute("selectedUseCaseIds", selectedUseCaseIds);

        return "crccards/edit";
    }

    @PostMapping("/{crcCardId}/edit")
    public String updateCRCCard(@PathVariable Long projectId,
                                @PathVariable Long crcCardId,
                                @ModelAttribute CRCCard crcCard,
                                @RequestParam(required = false) List<Long> useCaseIds) {
        crcCardService.updateCRCCard(crcCardId, crcCard, useCaseIds);

        return "redirect:/projects/" + projectId + "/crc-cards";
    }

    @PostMapping("/{crcCardId}/delete")
    public String deleteCRCCard(@PathVariable Long projectId,
                                @PathVariable Long crcCardId) {
        crcCardService.deleteCRCCard(crcCardId);

        return "redirect:/projects/" + projectId + "/crc-cards";
    }
}