package sorokin.entry;


public class PathsEntry {

    private String monitorDir;
    private String handledFilesDir;
    private String failureFilesDir;
    private Integer monitoringPeriod;

    public String getMonitorDir() {
        return monitorDir;
    }

    public void setMonitorDir(String monitorDir) {
        this.monitorDir = monitorDir;
    }

    public String getHandledFilesDir() {
        return handledFilesDir;
    }

    public void setHandledFilesDir(String handledFilesDir) {
        this.handledFilesDir = handledFilesDir;
    }

    public String getFailureFilesDir() {
        return failureFilesDir;
    }

    public void setFailureFilesDir(String failureFilesDir) {
        this.failureFilesDir = failureFilesDir;
    }

    public Integer getMonitoringPeriod() {
        return monitoringPeriod;
    }

    public void setMonitoringPeriod(Integer monitoringPeriod) {
        this.monitoringPeriod = monitoringPeriod;
    }
}
