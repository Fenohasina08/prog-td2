import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

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

    public List<Player> createPlayers(List<Player> newPlayers) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getDBConnection();
            conn.setAutoCommit(false);

            // Vérifier si les joueurs existent déjà
            String checkSql = "SELECT id FROM player WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);

            for (Player player : newPlayers) {
                checkStmt.setInt(1, player.getId());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    conn.rollback();
                    throw new RuntimeException("Le joueur avec id=" + player.getId() + " existe déjà");
                }
            }

            // Insérer les nouveaux joueurs
            String insertSql = "INSERT INTO player (id, name, age, position) VALUES (?, ?, ?, CAST(? AS position_type))";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);

            List<Player> createdPlayers = new ArrayList<>();
            for (Player player : newPlayers) {
                insertStmt.setInt(1, player.getId());
                insertStmt.setString(2, player.getName());
                insertStmt.setInt(3, player.getAge());
                insertStmt.setString(4, player.getPosition().name());
                insertStmt.executeUpdate();
                createdPlayers.add(player);
            }

            conn.commit();
            return createdPlayers;

        } catch (SQLException | RuntimeException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    // Ignorer
                }
            }
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException(e);
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    // Ignorer
                }
            }
        }
    }

    public List<Team> findTeamsByPlayerName(String playerName) throws SQLException {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT DISTINCT t.* FROM team t " +
                "INNER JOIN player p ON t.id = p.id_team " +
                "WHERE LOWER(p.name) LIKE LOWER(?) " +
                "ORDER BY t.id";

        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + playerName + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Team team = new Team();
                team.setId(rs.getInt("id"));
                team.setName(rs.getString("name"));
                team.setContinent(ContinentEnum.valueOf(rs.getString("continent")));
                teams.add(team);
            }
        }
        return teams;
    }

    public List<Player> findPlayersByCriteria(String playerName, PlayerPositionEnum position,
                                              String teamName, ContinentEnum continent,
                                              int page, int size) throws SQLException {
        List<Player> players = new ArrayList<>();

        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT p.*, t.name as team_name FROM player p " +
                        "LEFT JOIN team t ON p.id_team = t.id " +
                        "WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        if (playerName != null && !playerName.trim().isEmpty()) {
            sqlBuilder.append(" AND LOWER(p.name) LIKE LOWER(?)");
            params.add("%" + playerName + "%");
        }

        if (position != null) {
            sqlBuilder.append(" AND p.position = CAST(? AS position_type)");
            params.add(position.name());
        }

        if (teamName != null && !teamName.trim().isEmpty()) {
            sqlBuilder.append(" AND LOWER(t.name) LIKE LOWER(?)");
            params.add("%" + teamName + "%");
        }

        if (continent != null) {
            sqlBuilder.append(" AND t.continent = CAST(? AS continent_type)");
            params.add(continent.name());
        }

        sqlBuilder.append(" ORDER BY p.id LIMIT ? OFFSET ?");
        int offset = (page - 1) * size;
        params.add(size);
        params.add(offset);

        try (Connection conn = DBConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Player player = new Player();
                player.setId(rs.getInt("id"));
                player.setName(rs.getString("name"));
                player.setAge(rs.getInt("age"));
                player.setPosition(PlayerPositionEnum.valueOf(rs.getString("position")));

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