package sorokin.dao.xmldao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sorokin.dao.connection.ConnectionFactory;
import sorokin.dao.connection.ConnectionFactoryC3P0;
import sorokin.dao.tx.TransactionManager;
import sorokin.dao.tx.TransactionManagerImpl;
import sorokin.entry.XmlEntry;

import java.sql.Connection;
import java.text.ParseException;

public class XmlDaoImplTest {

    private XmlDaoImpl xmlDao;
    private TransactionManager tx;
    private ConnectionFactory conn;
    private XmlEntry xmlEntry;

    @Before
    public void setUp() throws ParseException {
        tx = new TransactionManagerImpl();
        conn = new ConnectionFactoryC3P0();
        tx.setConnectionFactory(conn);
        xmlEntry = Mockito.mock(XmlEntry.class);
        xmlDao = new XmlDaoImpl();
        xmlDao.setDatasource(tx);
    }

    @Test()
    public void getConnectionNullTest(){
        xmlDao.setDatasource(null);
        Connection connection = xmlDao.getConnection();
        Assert.assertNull(connection);
    }

    @Test
    public void insertNullTest(){
        Integer result = xmlDao.insert(xmlEntry);
        Assert.assertEquals(result, new Integer(0));
    }

    @Test
    public void insertExceptionTest(){
        xmlDao.setDatasource(null);
        Integer result = xmlDao.insert(xmlEntry);
        Assert.assertEquals(result, new Integer(0));
    }
}
