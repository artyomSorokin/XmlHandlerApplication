package sorokin.dao.xmldao;

import sorokin.entry.XmlEntry;

import javax.sql.DataSource;

public interface XmlDao {

    public void setDatasource(DataSource datasource) throws Exception;

    public Integer insert(XmlEntry xmlEntry);

     /*public List<XmlEntry> selectAll() throws SQLException, IOException;

    public XmlEntry selectById(Integer id) throws SQLException;

    public XmlEntry selectByFileName(String fileName) throws SQLException;*/

}
