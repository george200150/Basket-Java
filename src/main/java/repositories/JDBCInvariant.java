package repositories;

import loggers.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCInvariant {
    private static Connection connection = null;
    private static Properties jdbcProps = null;

    public JDBCInvariant(Properties props) {
        jdbcProps = props;
    }

    public static Connection getConnection(){

        String driver = jdbcProps.getProperty("jdbc.postgres.driver");
        String url = jdbcProps.getProperty("jdbc.postgres.server");
        String user = jdbcProps.getProperty("jdbc.postgres.user");
        String password = jdbcProps.getProperty("jdbc.postgres.password");
        try {
            Log.logger.traceEntry("entry constructor");
            Class.forName(driver);
            connection = DriverManager
                    .getConnection(url, user, password);
            Log.logger.info("successful connection");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.logger.error("connection failure"  + e.getMessage());
        }
        Log.logger.traceExit("successful exit", connection);
        //TODO: maybe should throw ERROR here in order to notify database NOT REACHABLE ???
        return connection;
    }
}
