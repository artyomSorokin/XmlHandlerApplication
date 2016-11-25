package sorokin.workprocess;

import org.apache.log4j.Logger;
import sorokin.xmlparser.XmlParser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkWithDirectory implements Runnable{

    private Logger log = Logger.getLogger(WorkWithDirectory.class);
    private XmlParser xmlParser;

    public void setXmlParser(XmlParser xmlParser) {
        log.info("Set Xmlparser");
        this.xmlParser = xmlParser;
    }

    public void run() {
        log.info("Create threadPool");
        ExecutorService service = Executors.newFixedThreadPool(5);
        log.info("Start to execute XmlParser");
        service.submit(xmlParser);
        service.shutdown();
    }
}
