package sorokin.dao.connection;



import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.Logger;
import sorokin.entry.DbConnectionEntry;

import java.io.FileInputStream;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactoryC3P0 implements ConnectionFactory{

    protected ComboPooledDataSource dataSource;
    private static final String DB_DRIVER = "org.postgresql.Driver";
    private Logger log = Logger.getLogger(ConnectionFactory.class);
    private DbConnectionEntry dbConnection;

    protected DbConnectionEntry readConfig(){
        log.info("read configurations for database connection");
        InputStream fis;
        Properties properties = new Properties();

        try {
            fis = getClass().getClassLoader().getResourceAsStream("config.properties");
            properties.load(fis);
            log.info("Load file config");
            dbConnection = new DbConnectionEntry();
            dbConnection.setNameDb(properties.getProperty("name_database"));
            dbConnection.setLogin(properties.getProperty("login_database"));
            dbConnection.setPassword(properties.getProperty("password_database"));
        }
        catch (Exception e){
            log.error("Error load file");
            log.error(e.getMessage());
        }
        return dbConnection;
    }

    public ConnectionFactoryC3P0() {
        createConnectionFactory();
    }


    public Connection newConnection(){
        Connection connection = null;
        try {
            log.info("Get connection");
            connection = dataSource.getConnection();
        }
        catch (Exception e){
            log.error("Problem with connection");
            log.error(e.getMessage());
        }
        return connection;
    }

    public void createConnectionFactory(){
        try{
            log.info("Create pool connection for database");
            this.dataSource = new ComboPooledDataSource();
            DbConnectionEntry dbConnectionEntry = readConfig();
            dataSource.setDriverClass(DB_DRIVER);
            dataSource.setJdbcUrl(dbConnectionEntry.getNameDb());
            dataSource.setUser(dbConnectionEntry.getLogin());
            dataSource.setPassword(dbConnectionEntry.getPassword());

            dataSource.setMinPoolSize(1);
            dataSource.setAcquireIncrement(1);
            dataSource.setMaxPoolSize(20);

            dataSource.setMaxStatements(180);
            dataSource.setMaxStatementsPerConnection(10);
            log.info("Creating pool connection is successful");
        } catch (Exception e) {
            log.error("Problem with DB connection");
            log.error(e.getMessage());
        }
    }

    public void close() throws SQLException {
        //NOP
    }
}
