import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Test de la connexion à la base de données ===");
        
        DBConnection.testConnection();
        
        DataRetriever dataRetriever = new DataRetriever();
        
        try {
            System.out.println("\n=== Test a) findTeamById(1) ===");
            Team team1 = dataRetriever.findTeamById(1);
            if (team1 != null) {
                System.out.println("✅ Équipe trouvée : " + team1.getName());
                System.out.println("   Continent : " + team1.getContinent());
                System.out.println("   Nombre de joueurs : " + team1.getPlayersCount());
                for (Player player : team1.getPlayers()) {
                    System.out.println("   - " + player.getName() + " (" + player.getPosition() + ")");
                }
            } else {
                System.out.println("❌ Aucune équipe trouvée avec id=1");
            }
            
            System.out.println("\n=== Test b) findTeamById(5) ===");
            Team team5 = dataRetriever.findTeamById(5);
            if (team5 != null) {
                System.out.println("✅ Équipe trouvée : " + team5.getName());
                System.out.println("   Continent : " + team5.getContinent());
                System.out.println("   Nombre de joueurs : " + team5.getPlayersCount());
            } else {
                System.out.println("❌ Aucune équipe trouvée avec id=5");
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
