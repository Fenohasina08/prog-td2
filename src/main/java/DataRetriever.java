import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    // a) Trouver une équipe par son ID avec ses joueurs
    public Team findTeamById(Integer id) throws SQLException {
        Team team = null;
        String sqlTeam = "SELECT * FROM team WHERE id = ?";
        String sqlPlayers = "SELECT * FROM player WHERE id_team = ?";

        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement stmtTeam = conn.prepareStatement(sqlTeam)) {

            stmtTeam.setInt(1, id);
            ResultSet rsTeam = stmtTeam.executeQuery();

            if (rsTeam.next()) {
                team = new Team();
                team.setId(rsTeam.getInt("id"));
                team.setName(rsTeam.getString("name"));
                team.setContinent(ContinentEnum.valueOf(rsTeam.getString("continent")));

                // Récupérer les joueurs de l'équipe
                try (PreparedStatement stmtPlayers = conn.prepareStatement(sqlPlayers)) {
                    stmtPlayers.setInt(1, id);
                    ResultSet rsPlayers = stmtPlayers.executeQuery();

                    List<Player> players = new ArrayList<>();
                    while (rsPlayers.next()) {
                        Player player = new Player();
                        player.setId(rsPlayers.getInt("id"));
                        player.setName(rsPlayers.getString("name"));
                        player.setAge(rsPlayers.getInt("age"));
                        player.setPosition(PlayerPositionEnum.valueOf(rsPlayers.getString("position")));
                        player.setTeam(team);
                        players.add(player);
                    }
                    team.setPlayers(players);
                }
            }
        }
        return team;
    }

    // b) Récupérer les joueurs avec pagination
    public List<Player> findPlayers(int page, int size) throws SQLException {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT p.*, t.name as team_name FROM player p " +
                "LEFT JOIN team t ON p.id_team = t.id " +
                "ORDER BY p.id LIMIT ? OFFSET ?";

        int offset = (page - 1) * size;

        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, size);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Player player = new Player();
                player.setId(rs.getInt("id"));
                player.setName(rs.getString("name"));
                player.setAge(rs.getInt("age"));
                player.setPosition(PlayerPositionEnum.valueOf(rs.getString("position")));

                // Si le joueur a une équipe
                if (rs.getInt("id_team") > 0) {
                    Team team = new Team();
                    team.setId(rs.getInt("id_team"));
                    team.setName(rs.getString("team_name"));
                    player.setTeam(team);
                }

                players.add(player);
            }
        }
        return players;
    }
}