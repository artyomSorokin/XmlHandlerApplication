package sorokin.workprocess;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sorokin.entry.PathsEntry;

public class ReadConfigFileTest {

    private ReadConfigFile readConfigFile;

    @Before
    public void setUp(){
        readConfigFile = new ReadConfigFile();
    }

    @Test
    public void getPathsEntry(){
        PathsEntry pathsEntry = readConfigFile.getPathsEntry();
        Assert.assertNotNull(pathsEntry);
        Assert.assertTrue(pathsEntry instanceof PathsEntry);
        Assert.assertTrue(pathsEntry.getMonitorDir() instanceof String);
        Assert.assertTrue(pathsEntry.getHandledFilesDir() instanceof String);
        Assert.assertTrue(pathsEntry.getFailureFilesDir() instanceof String);
        Assert.assertTrue(pathsEntry.getMonitoringPeriod() instanceof Integer);
    }
}
