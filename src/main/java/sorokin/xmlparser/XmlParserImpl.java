package sorokin.xmlparser;

import org.apache.log4j.Logger;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import sorokin.dao.tx.TransactionManager;
import sorokin.dao.xmldao.XmlDao;
import sorokin.entry.PathsEntry;
import sorokin.entry.XmlEntry;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;


public class XmlParserImpl implements XmlParser,Runnable {

    private static final String ENTRY = "Entry";
    private static final String CONTENT = "content";
    private static final String CREATION_DATE = "creationDate";
    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private TransactionManager tx;
    private XmlDao xmlDao;
    private PathsEntry pathsEntry;
    protected volatile Map<String,String> fileMapXml = new ConcurrentHashMap<>();
    private Logger log = Logger.getLogger(XmlParser.class);

    @Override
    public void setPathsEntry(PathsEntry pathsEntry) {
        this.pathsEntry = pathsEntry;
    }

    @Override
    public void setTx(TransactionManager tx) {
        this.tx = tx;
    }

    @Override
    public void setXmlDao(XmlDao xmlDao) {
        this.xmlDao = xmlDao;
    }

    protected void getFileListXML(){
        log.info("Get list File of monitoring directory");
        File dir = new File(pathsEntry.getMonitorDir());
        if(!dir.exists()){
            log.info("Create directory");
            dir.mkdirs();
        }
        log.info("Monitoring directory exists");
        File[] files = dir.listFiles();
        for(File file : files){
            if(file.isFile()){
                if(file.getName().endsWith(".xml")){
                    log.info("Put fileName.xml to the local map");
                    fileMapXml.put(file.getName(),file.getAbsolutePath());
                }
            }
        }
    }

    protected void readFile() {
        File file = null;
        log.info("Start to read file");
        if (!fileMapXml.isEmpty()) {
            for (Map.Entry<String, String> entry : fileMapXml.entrySet()) {
                try {
                    file = new File(entry.getValue());
                    log.info("Send file to parseXmlFile");
                    Integer successWriteDB = parseXmlFile(entry.getValue());
                    if(successWriteDB == 0){
                        log.error("File din't write to database");
                    }
                    else {
                        File handledDir = new File(pathsEntry.getHandledFilesDir());
                        if (!handledDir.exists()) {
                            log.info("Create handled directory");
                            handledDir.mkdirs();
                        }
                        log.info("Handled directory exists");
                        File existFile = new File(pathsEntry.getHandledFilesDir() + File.separator + entry.getKey());
                        if (existFile.exists()) {
                            log.info("If file exists in handled directory, we delete this file");
                            existFile.delete();
                        }
                        log.info("Move this file to the handled directory");
                        file.renameTo(new File(handledDir, entry.getKey()));
                        log.info("Delete handled file from local map");
                        fileMapXml.remove(entry.getKey());
                    }
                } catch (Exception e) {
                    moveToFailureDir(file, entry.getKey());
                }
            }
        }
    }


    protected Integer parseXmlFile(String path) throws Exception{
        Integer successWriteDB = 0;
        log.info("Start parse xml file");
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(path);
        XmlEntry xmlEntry = new XmlEntry();
        log.info("Create Document");
        Document document = (Document) builder.build(xmlFile);
        log.info("Get content of document");
        List<Content> documentContent = document.getContent();
        for (int i = 0; i < documentContent.size(); i++) {
            log.info("Get root of document");
            Element rootNode = (Element) documentContent.get(i);
            if (rootNode.getName().equals(ENTRY)) {
                log.info("Root name is entry");
                String content = rootNode.getChildText(CONTENT);
                log.info("Get content");
                if(content.length()<1024){
                    log.info("Content < 1024");
                    xmlEntry.setContent(content);
                    log.info("Get creation date");
                    String date = rootNode.getChildText(CREATION_DATE);
                    xmlEntry.setCreationDate(parseDate(date));
                    log.info("Get file name");
                    xmlEntry.setFileName(xmlFile.getName());
                    log.info("start write to database");
                    successWriteDB = writeToDB(xmlEntry);
                }
                else{
                    //NOP
                }
            }
            else{
                //NOP
            }
        }
        return successWriteDB;
    }

    private Date parseDate(String date) throws ParseException {
        log.info("Parse creation date");
        if (date == null){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        return simpleDateFormat.parse(date);
    }

    protected Integer writeToDB(final XmlEntry xmlEntry){
        try {
            log.info("Set datasource to database");

            xmlDao.setDatasource(tx);
            log.info("Start transaction");
            Integer n = tx.doInTransaction(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    log.info("write to database");
                    return xmlDao.insert(xmlEntry);
                }
            });
        }
        catch(Exception e){
            log.error("Problem with database");
            log.error(e.getMessage());
            return 0;
        }
        return 1;
    }

    protected void moveToFailureDir(File file, String fileName){
        File failureDir = new File(pathsEntry.getFailureFilesDir());
        if (!failureDir.exists()) {
            log.info("Create failure directory");
            failureDir.mkdirs();
        }
        File existFile = new File(pathsEntry.getFailureFilesDir() + File.separator + fileName);
        if (existFile.exists()) {
            log.info("If file exists in failure directory, we delete this file");
            existFile.delete();
        }
        log.info("Move this file to the failure directory");
        file.renameTo(new File(failureDir, fileName));
        fileMapXml.remove(fileName);
    }

    @Override
    public void run() {
        log.info("Start run");
        log.info("Start time block code");
        long start = System.currentTimeMillis();
        getFileListXML();
        readFile();
        long end = System.currentTimeMillis();
        long result = end-start;
        log.info("Time work main part of program is " + result);
    }

}
