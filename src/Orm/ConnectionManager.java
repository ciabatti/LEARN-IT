package Orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String url = "jdbc:postgresql://localhost:5433/myCourseAppdb";
    private static final String username = "postgres";
    private static final String password = "psw";
    private static Connection connector = null;

    private ConnectionManager(){}

    static public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        if (connector == null)
            connector = DriverManager.getConnection(url, username, password);

        return connector;
    }
}

