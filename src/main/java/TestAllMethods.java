import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestAllMethods {
    public static void main(String[] args) {
        DataRetriever dataRetriever = new DataRetriever();
        
        System.out.println("========== TESTS COMPLETS (a à j) ==========\n");
        
        try {
            // TEST a
            System.out.println("a) findTeamById(1):");
            Team team1 = dataRetriever.findTeamById(1);
            if (team1 != null && team1.getPlayersCount() == 3) {
                System.out.println("   ✅ OK - " + team1.getName() + " avec " + team1.getPlayersCount() + " joueurs");
            } else {
                System.out.println("   ❌ ÉCHEC");
            }
            
            // TEST b
            System.out.println("\nb) findTeamById(5):");
            Team team5 = dataRetriever.findTeamById(5);
            if (team5 != null && team5.getPlayersCount() == 0) {
                System.out.println("   ✅ OK - " + team5.getName() + " avec " + team5.getPlayersCount() + " joueurs");
            } else {
                System.out.println("   ❌ ÉCHEC");
            }
            
            // TEST c
            System.out.println("\nc) findPlayers(1, 2):");
            List<Player> playersPage1 = dataRetriever.findPlayers(1, 2);
            if (playersPage1.size() == 2) {
                System.out.println("   ✅ OK - 2 joueurs trouvés");
            } else {
                System.out.println("   ❌ ÉCHEC - " + playersPage1.size() + " joueurs");
            }
            
            // TEST d
            System.out.println("\nd) findPlayers(3, 5):");
            List<Player> playersPage3 = dataRetriever.findPlayers(3, 5);
            if (playersPage3.isEmpty()) {
                System.out.println("   ✅ OK - Liste vide");
            } else {
                System.out.println("   ❌ ÉCHEC - " + playersPage3.size() + " joueurs");
            }
            
            // TEST e
            System.out.println("\ne) findTeamsByPlayerName(\"an\"):");
            List<Team> teamsWithAn = dataRetriever.findTeamsByPlayerName("an");
            System.out.println("   Trouvé " + teamsWithAn.size() + " équipes");
            for (Team t : teamsWithAn) {
                System.out.println("   - " + t.getName());
            }
            
            // TEST f
            System.out.println("\nf) findPlayersByCriteria:");
            List<Player> filteredPlayers = dataRetriever.findPlayersByCriteria(
                "ud", PlayerPositionEnum.MIDF, "Madrid", ContinentEnum.EUROPA, 1, 10);
            System.out.println("   Trouvé " + filteredPlayers.size() + " joueurs");
            for (Player p : filteredPlayers) {
                System.out.println("   - " + p.getName());
            }
            
            // TEST g
            System.out.println("\ng) createPlayers avec joueur existant:");
            try {
                Player existingPlayer = new Player(1, "Jude Bellingham", 23, PlayerPositionEnum.STR);
                Player newPlayer = new Player(8, "Pedri", 24, PlayerPositionEnum.MIDF);
                dataRetriever.createPlayers(Arrays.asList(existingPlayer, newPlayer));
                System.out.println("   ❌ ÉCHEC - Devait lever RuntimeException");
            } catch (RuntimeException e) {
                System.out.println("   ✅ OK - RuntimeException levée");
            }
            
            // TEST h
            System.out.println("\nh) createPlayers avec nouveaux joueurs:");
            Player vini = new Player(9, "Vini", 25, PlayerPositionEnum.STR);
            Player pedri = new Player(10, "Pedri", 24, PlayerPositionEnum.MIDF);
            try {
                List<Player> created = dataRetriever.createPlayers(Arrays.asList(vini, pedri));
                System.out.println("   ✅ OK - " + created.size() + " joueurs créés");
            } catch (RuntimeException e) {
                System.out.println("   ⚠️  " + e.getMessage());
            }
            
            System.out.println("\n========== TESTS TERMINÉS ==========");
            
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
