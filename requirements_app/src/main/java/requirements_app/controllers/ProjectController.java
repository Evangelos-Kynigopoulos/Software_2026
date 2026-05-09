package requirements_app.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import requirements_app.domain.Project;
import requirements_app.services.ProjectService;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public String listProjects(Model model, Authentication authentication) {
        String username = authentication.getName();

        model.addAttribute("projects", projectService.findProjectsForUser(username));

        return "projects/list";
    }

    @GetMapping("/new")
    public String newProjectForm(Model model) {
        model.addAttribute("project", new Project());

        return "projects/new";
    }

    @PostMapping
    public String createProject(@ModelAttribute Project project, Authentication authentication) {
        String username = authentication.getName();

        projectService.createProject(username, project);

        return "redirect:/projects";
    }

    @GetMapping("/{id}/edit")
    public String editProjectForm(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.findProjectById(id));

        return "projects/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateProject(@PathVariable Long id, @ModelAttribute Project project) {
        projectService.updateProject(id, project);

        return "redirect:/projects";
    }

    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);

        return "redirect:/projects";
    }
}