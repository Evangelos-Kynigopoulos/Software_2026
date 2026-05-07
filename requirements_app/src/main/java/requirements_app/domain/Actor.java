package requirements_app.domain;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToMany(mappedBy = "actors")
    private List<UseCase> useCases;

    public Actor() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public List<UseCase> getUseCases() { return useCases; }
    public void setUseCases(List<UseCase> useCases) { this.useCases = useCases; }
}
