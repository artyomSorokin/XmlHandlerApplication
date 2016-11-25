package sorokin.workprocess;



import org.apache.log4j.Logger;
import sorokin.dao.connection.ConnectionFactory;
import sorokin.dao.connection.ConnectionFactoryC3P0;
import sorokin.dao.tx.TransactionManager;
import sorokin.dao.tx.TransactionManagerImpl;
import sorokin.dao.xmldao.XmlDao;
import sorokin.dao.xmldao.XmlDaoImpl;
import sorokin.entry.PathsEntry;
import sorokin.xmlparser.XmlParser;
import sorokin.xmlparser.XmlParserImpl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        log.info("Start program");
        ReadConfigFile readConfigFile = new ReadConfigFile();
        PathsEntry pathsEntry = readConfigFile.getPathsEntry();
        log.info("read the file");

        log.info("Set parameters for Database");
        ConnectionFactory connectionFactory = new ConnectionFactoryC3P0();
        TransactionManager tx = new TransactionManagerImpl();
        tx.setConnectionFactory(connectionFactory);
        XmlDao xmlDao = new XmlDaoImpl();

        log.info("Set parameters for XmlParser");
        XmlParser xmlParser = new XmlParserImpl();
        xmlParser.setXmlDao(xmlDao);
        xmlParser.setTx(tx);
        xmlParser.setPathsEntry(pathsEntry);

        log.info("start work with file system");
        WorkWithDirectory workWithDirectory = new WorkWithDirectory();
        workWithDirectory.setXmlParser(xmlParser);

        //thread for monitoring directory
        log.info("run thread for monitoring directory");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleWithFixedDelay(workWithDirectory, 0, pathsEntry.getMonitoringPeriod(), TimeUnit.SECONDS);
    }
}
