package sorokin.dao.tx;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sorokin.dao.connection.ConnectionFactory;
import sorokin.dao.connection.ConnectionFactoryC3P0;
import sorokin.dao.xmldao.XmlDao;
import sorokin.dao.xmldao.XmlDaoImpl;
import sorokin.entry.XmlEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.Callable;

public class TransactionManagerImplTest {

    private TransactionManager tx;
    private XmlEntry xmlEntry;
    private XmlDao xmlDao;
    private XmlEntry xmlCorrectEntry;
    private ConnectionFactory conn;

    @Before
    public void setUp() throws Exception {
        tx = new TransactionManagerImpl();
        conn = new ConnectionFactoryC3P0();
        tx.setConnectionFactory(conn);
        xmlEntry = Mockito.mock(XmlEntry.class);
        xmlDao = new XmlDaoImpl();
        xmlCorrectEntry = new XmlEntry();
        xmlCorrectEntry.setContent("hgyt");
        xmlCorrectEntry.setCreationDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2012-01-01 00:00:00"));
        xmlCorrectEntry.setFileName("dhtry");
        xmlDao.setDatasource(tx);
    }

    @Test
    public void doInTransactionNullTest() throws Exception {
        Object object = tx.doInTransaction(Mockito.mock(Callable.class));
        Assert.assertNull(object);
    }

    @Test
    public void doInTransactionExceptionInDBTest() throws Exception {
        Integer i = tx.doInTransaction(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int n = xmlDao.insert(xmlEntry);
                return n;
            }
        });
        Assert.assertNotNull(i);
        Assert.assertEquals(i, new Integer(0));
    }

    @Test
    public void doInTransactionCorrectWriteDBTest() throws Exception {
        Integer i = tx.doInTransaction(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int n = xmlDao.insert(xmlCorrectEntry);
                return n;
            }
        });
        Assert.assertNotNull(i);
        Assert.assertEquals(i, new Integer(1));
    }

    @Test(expected = Exception.class)
    public void doInTransactionExceptionTest() throws Exception {
        tx.setConnectionFactory(null);
        Integer i = tx.doInTransaction(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int n = xmlDao.insert(xmlEntry);
                return n;
            }
        });
        Assert.assertNotNull(i);
        Assert.assertEquals(i, new Integer(0));
    }
}
