package sorokin.workprocess;

import org.apache.log4j.Logger;
import sorokin.entry.PathsEntry;
import java.io.InputStream;
import java.util.Properties;

public class ReadConfigFile {

    private Logger log = Logger.getLogger(ReadConfigFile.class);
    private InputStream fis;
    private Properties properties = new Properties();
    private PathsEntry paths;

    //read Config paths from file
    public PathsEntry getPathsEntry(){
        try {
            fis = getClass().getClassLoader().getResourceAsStream("config.properties");
            properties.load(fis);
            log.info("Load file config");
            paths = new PathsEntry();
            paths.setMonitorDir(properties.getProperty("monitoring_directory"));
            paths.setHandledFilesDir(properties.getProperty("handled_files_directory"));
            paths.setFailureFilesDir(properties.getProperty("failure_files_directory"));
            Integer period = Integer.parseInt(properties.getProperty("monitoring_period"));
            paths.setMonitoringPeriod(period);
            log.info("Set directories for monitoring, handled and error files");
        }
        catch (Exception e){
            log.error("Error load file");
            log.error(e.getMessage());
        }
        return paths;
    }
}
