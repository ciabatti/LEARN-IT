package orm;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

public class ConnectionManagerTest {

    private static final String URL = "jdbc:postgresql://localhost:5433/myCourseAppdb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "psw";

    @Test
    public void testDatabaseConnection() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            assertNotNull(conn);

        } catch (SQLException e) {
            fail("Connessione fallita: " + e.getMessage());
        }
    }
}
