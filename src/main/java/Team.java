import java.util.ArrayList;
import java.util.List;

public class Team {
    private int id;
    private String name;
    private ContinentEnum continent;
    private List<Player> players = new ArrayList<>();

    // Constructeurs
    public Team() {}

    public Team(int id, String name, ContinentEnum continent) {
        this.id = id;
        this.name = name;
        this.continent = continent;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContinentEnum getContinent() {
        return continent;
    }

    public void setContinent(ContinentEnum continent) {
        this.continent = continent;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    // Méthode pour ajouter un joueur
    public void addPlayer(Player player) {
        if (players == null) {
            players = new ArrayList<>();
        }
        players.add(player);
    }

    // Méthode getPlayersCount() demandée
    public Integer getPlayersCount() {
        return players != null ? players.size() : 0;
    }

    @Override
    public String toString() {
        return "Team{id=" + id + ", name='" + name + "', continent=" + continent +
                ", playersCount=" + getPlayersCount() + "}";
    }
}