package db;

import java.sql.*;

public class DBContext {

    private Connection connection;
    private final Object lock = new Object();

    private void createConnection() {
        synchronized (lock) {
            if (connection == null) {
                try {
                    Class.forName("org.postgresql.Driver");

                    String url = "jdbc:postgresql://localhost:5432/postgres";
                    String username = "postgres";
                    String password = "0000";

                    connection = DriverManager.getConnection(url, username, password);

                    String query = "CREATE TABLE IF NOT EXISTS users (login VARCHAR, password VARCHAR, email VARCHAR)";
                    execUpdate(query);
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void execUpdate(String query, Object... params) {
        createConnection();

        synchronized (lock) {
            try(PreparedStatement statement = connection.prepareStatement(query)) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T execQuery(String query, ResultHandler<T> handler, Object... params) {
        createConnection();

        synchronized (lock) {
            try(PreparedStatement statement = connection.prepareStatement(query)) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }

                ResultSet result = statement.executeQuery();
                if (!result.next()) {
                    return null;
                }

                return handler.handle(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
