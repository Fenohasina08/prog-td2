import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/mini_football_db";
    private static final String USERNAME = "mini_football_db_manager";
    private static final String PASSWORD = "123456";

    // Méthode pour obtenir une connexion
    public static Connection getDBConnection() throws SQLException {
        try {
            // Charger le driver PostgreSQL
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver non trouvé", e);
        }

        // Établir la connexion
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    // Version avec variables d'environnement (selon le sujet)
    public static Connection getDBConnectionFromEnv() throws SQLException {
        // Récupérer les variables d'environnement
        String jdbcUrl = System.getenv("JDBC_URL");
        String username = System.getenv("USERNAME");
        String password = System.getenv("PASSWORD");

        // Utiliser les valeurs par défaut si les variables ne sont pas définies
        if (jdbcUrl == null || jdbcUrl.isEmpty()) {
            jdbcUrl = JDBC_URL;
        }
        if (username == null || username.isEmpty()) {
            username = USERNAME;
        }
        if (password == null) {
            password = PASSWORD;
        }

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver non trouvé", e);
        }

        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    // Méthode pour tester la connexion
    public static void testConnection() {
        try (Connection conn = getDBConnection()) {
            if (conn != null) {
                System.out.println("✅ Connexion à la base de données établie avec succès !");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion à la base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }
}