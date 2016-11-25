package sorokin.dao.connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sorokin.entry.DbConnectionEntry;

import java.sql.Connection;
import java.sql.SQLException;


public class ConnectionFactoryC3P0Test {

    private ConnectionFactoryC3P0 conn;
    private Connection connection;

    @Before
    public void setUp(){
        conn = new ConnectionFactoryC3P0();
    }

    @Test
    public void readConfigTest(){
        DbConnectionEntry dbConnectionEntry = conn.readConfig();
        Assert.assertNotNull(dbConnectionEntry);
        Assert.assertTrue(dbConnectionEntry instanceof DbConnectionEntry);
    }

    @Test
    public void newConnectionNotNullTest() throws SQLException {
        connection = conn.newConnection();
        Assert.assertNotNull(connection);
        Assert.assertTrue(connection instanceof Connection);
    }

    @Test
    public void newConnectionIsNullTest() throws SQLException {
        conn.dataSource = null;
        connection = conn.newConnection();
        Assert.assertNull(connection);
    }

    @Test
    public void createConnectionFactoryTest(){
        conn.createConnectionFactory();
        Assert.assertNotNull(conn.dataSource);
        Assert.assertTrue(conn.dataSource instanceof ComboPooledDataSource);
    }
}
