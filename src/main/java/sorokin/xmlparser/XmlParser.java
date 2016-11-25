package sorokin.xmlparser;


import sorokin.dao.tx.TransactionManager;
import sorokin.dao.xmldao.XmlDao;
import sorokin.entry.PathsEntry;

public interface XmlParser extends Runnable{

    public void setTx(TransactionManager tx);

    public void setXmlDao(XmlDao xmlDao);

    public void setPathsEntry(PathsEntry pathsEntry);
}
