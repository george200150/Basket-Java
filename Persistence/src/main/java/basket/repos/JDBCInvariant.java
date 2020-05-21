package basket.repos;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCInvariant {
    private static Connection connection = null;
    private static Properties jdbcProps = null;
    static final Logger logger = LogManager.getLogger(JDBCInvariant.class);

    public JDBCInvariant(Properties props) {
        jdbcProps = props;
    }

    public static Connection getConnection(){

        String driver = jdbcProps.getProperty("jdbc.postgres.driver");
        String url = jdbcProps.getProperty("jdbc.postgres.server");
        String user = jdbcProps.getProperty("jdbc.postgres.user");
        String password = jdbcProps.getProperty("jdbc.postgres.password");
        try {
            logger.traceEntry("entry constructor");
            Class.forName(driver);
            connection = DriverManager
                    .getConnection(url, user, password);
            logger.info("successful connection");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("connection failure"  + e.getMessage());
        }
        logger.traceExit("successful exit", connection);
        //TODO: maybe should throw ERROR here in order to notify database NOT REACHABLE ???
        return connection;
    }
}
