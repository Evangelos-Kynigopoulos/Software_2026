package requirements_app.services;

import org.springframework.stereotype.Service;
import requirements_app.domain.Project;
import requirements_app.domain.User;
import requirements_app.repositories.ProjectRepository;
import requirements_app.repositories.UserRepository;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<Project> findProjectsForUser(String username) {
        return projectRepository.findByUserUsername(username);
    }

    public Project findProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));
    }

    public Project createProject(String username, Project project) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        project.setUser(user);
        return projectRepository.save(project);
    }

    public Project updateProject(Long projectId, Project updatedProject) {
        Project existingProject = findProjectById(projectId);

        existingProject.setName(updatedProject.getName());
        existingProject.setDescription(updatedProject.getDescription());

        return projectRepository.save(existingProject);
    }

    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }
}