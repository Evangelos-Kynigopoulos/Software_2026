package requirements_app.domain;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class UseCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String preconditions;

    @Column(columnDefinition = "TEXT")
    private String mainFlow;

    @Column(columnDefinition = "TEXT")
    private String postconditions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToMany
    @JoinTable(
            name = "use_case_actor",
            joinColumns = @JoinColumn(name = "use_case_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> actors;

    @ManyToMany
    @JoinTable(
            name = "use_case_crc_card",
            joinColumns = @JoinColumn(name = "use_case_id"),
            inverseJoinColumns = @JoinColumn(name = "crc_card_id")
    )
    private List<CRCCard> crcCards;

    public UseCase() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPreconditions() { return preconditions; }
    public void setPreconditions(String preconditions) { this.preconditions = preconditions; }

    public String getMainFlow() { return mainFlow; }
    public void setMainFlow(String mainFlow) { this.mainFlow = mainFlow; }

    public String getPostconditions() { return postconditions; }
    public void setPostconditions(String postconditions) { this.postconditions = postconditions; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public List<Actor> getActors() { return actors; }
    public void setActors(List<Actor> actors) { this.actors = actors; }

    public List<CRCCard> getCrcCards() { return crcCards; }
    public void setCrcCards(List<CRCCard> crcCards) { this.crcCards = crcCards; }
}
