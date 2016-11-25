package sorokin.xmlparser;
import org.jdom2.input.JDOMParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import sorokin.dao.connection.ConnectionFactory;
import sorokin.dao.connection.ConnectionFactoryC3P0;
import sorokin.dao.tx.TransactionManager;
import sorokin.dao.tx.TransactionManagerImpl;
import sorokin.dao.xmldao.XmlDao;
import sorokin.dao.xmldao.XmlDaoImpl;
import sorokin.entry.PathsEntry;
import sorokin.entry.XmlEntry;
import sorokin.workprocess.ReadConfigFile;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XmlParserImplTest {

    private XmlParserImpl xmlParser;
    private TransactionManager tx;
    private ConnectionFactory conn;
    private XmlDao xmlDao;
    private PathsEntry pathsEntry;

    @Before
    public void setUp(){
        xmlParser = new XmlParserImpl();
        tx = new TransactionManagerImpl();
        xmlDao = new XmlDaoImpl();
        conn = new ConnectionFactoryC3P0();
        pathsEntry = new ReadConfigFile().getPathsEntry();
        tx.setConnectionFactory(conn);
        xmlParser.setTx(tx);
        xmlParser.setXmlDao(xmlDao);
        xmlParser.setPathsEntry(pathsEntry);
    }

    @Test
    public void getFileListXMLNotNullTest(){
        xmlParser.getFileListXML();
        Assert.assertNotNull(xmlParser.fileMapXml);
        Assert.assertTrue(xmlParser.fileMapXml instanceof ConcurrentHashMap);
    }

    @Test(expected = Exception.class)
    public void getFileListXMLExceptionTest(){
        xmlParser.setPathsEntry(null);
        xmlParser.getFileListXML();
        Assert.assertNull(xmlParser.fileMapXml);
    }

    @Test
    public void getFileListXMLNullTest(){
        xmlParser.getFileListXML();
        Assert.assertNotNull(xmlParser.fileMapXml);
        Assert.assertTrue(xmlParser.fileMapXml instanceof ConcurrentHashMap);
        Assert.assertNull(xmlParser.fileMapXml.get(new String("")));
    }

    @Test
    public void getFileListXMLContainXmlFilesTest(){
        xmlParser.getFileListXML();
        for (Map.Entry<String, String> entry : xmlParser.fileMapXml.entrySet()) {
            Assert.assertTrue(entry.getValue().endsWith(".xml"));
        }
    }

    @Test
    public void readFileTest(){
        xmlParser.getFileListXML();
        int size = xmlParser.fileMapXml.keySet().size();
        xmlParser.readFile();
        Assert.assertEquals(xmlParser.fileMapXml.keySet().size(), size);
    }

    @Test(expected = JDOMParseException.class)
    public void parseXmlFileNullTest() throws Exception{
        Integer result = xmlParser.parseXmlFile("");
        Assert.assertEquals(result, new Integer(0));
    }

    @Test()
    public void writeToDB(){
        Integer result = xmlParser.writeToDB(Mockito.mock(XmlEntry.class));
        Assert.assertEquals(result, new Integer(1));
    }
}
