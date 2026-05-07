package requirements_app.domain;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;

    // User is called only when Project.getUser().getUsername() is called
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username") // Maps to the User's @Id
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UseCase> useCases;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CRCCard> crcCards;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Actor> actors;

    public Project() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<UseCase> getUseCases() { return useCases; }
    public void setUseCases(List<UseCase> useCases) { this.useCases = useCases; }

    public List<CRCCard> getCrcCards() { return crcCards; }
    public void setCrcCards(List<CRCCard> crcCards) { this.crcCards = crcCards; }

    public List<Actor> getActors() { return actors; }
    public void setActors(List<Actor> actors) { this.actors = actors; }
}