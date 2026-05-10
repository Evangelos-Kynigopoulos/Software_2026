package requirements_app.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CRCCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String className;

    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    @Column(columnDefinition = "TEXT")
    private String collaborations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToMany(mappedBy = "crcCards")
    private List<UseCase> useCases = new ArrayList<>();

    public CRCCard() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getResponsibilities() { return responsibilities; }
    public void setResponsibilities(String responsibilities) { this.responsibilities = responsibilities; }

    public String getCollaborations() { return collaborations; }
    public void setCollaborations(String collaborations) { this.collaborations = collaborations; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public List<UseCase> getUseCases() { return useCases; }
    public void setUseCases(List<UseCase> useCases) { this.useCases = useCases; }
}