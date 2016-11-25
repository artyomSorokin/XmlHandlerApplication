package sorokin.dao.xmldao;
import org.apache.log4j.Logger;
import sorokin.entry.XmlEntry;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;


/*import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;*/

public class XmlDaoImpl implements XmlDao{

    private static final String INSERT_SQL = "INSERT INTO xmlFiles(content, creationDate, fileName) values(?,?,?)";
    private DataSource dataSource;
    private Logger log = Logger.getLogger(XmlDao.class);

    /*private static final String SELECT_ALL_SQL = "SELECT id, content, creationDate, fileName FROM xmlFiles";
    private static final String CREATE_TABLE = "CREATE table xmlFiles(id integer(10) AUTO_INCREMENT," +
                                              " content TEXT(1024)," +
                                              " creationDate Date," +
                                              " fileName varchar,"+
                                              " PRIMARY KEY(id))";

    public static final String SELECT_BY_ID = "SELECT content, creationDate, fileName FROM xmlFiles WHERE id = ?";
    public static final String SELECT_BY_FILE_NAME = "SELECT id, content, creationDate FROM xmlFiles WHERE fileName = ?";*/






    @Override
    public void setDatasource(DataSource datasource) {
        this.dataSource = datasource;
    }

    @Override
    public Integer insert(XmlEntry xmlEntry) {
        try {
            log.info("Start insert methods");
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL);

            preparedStatement.setString(1, xmlEntry.getContent());
            preparedStatement.setDate(2, new Date(xmlEntry.getCreationDate().getTime()));
            preparedStatement.setString(3, xmlEntry.getFileName());
            preparedStatement.executeUpdate();

            preparedStatement.close();
            log.info("End insert methods");
        }
        catch (Exception e){
            log.error("Insert method don't work");
            log.error(e.getMessage());
            return 0;
        }
        return 1;
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            log.info("Get a connection");
            connection = dataSource.getConnection();
        }
        catch (Exception e){
            log.error("Prodlem with connection");
            log.error(e.getMessage());
        }
        return connection;
    }


    //if you want to work with database, you will be need this methods

    /*@Override
    public List<XmlEntry> selectAll() throws SQLException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<XmlEntry> xmlEntryList = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(SELECT_ALL_SQL);
            System.out.println("rs");
            xmlEntryList = selectWithResulSet(rs);
        }
        finally {
            rs.close();
            statement.close();
        }
        return xmlEntryList;
    }*/

    /*@Override
    public XmlEntry selectById(Integer id) throws SQLException {
        XmlEntry xmlEntry = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SELECT_BY_ID);
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                xmlEntry = new XmlEntry();
                xmlEntry.setId(id);
                xmlEntry.setContent(rs.getString("content"));
                xmlEntry.setCreationDate(rs.getTimestamp("creationDate"));
                xmlEntry.setFileName(rs.getString("fileName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rs.close();
            preparedStatement.close();
        }
        return xmlEntry;
    }

    @Override
    public XmlEntry selectByFileName(String fileName) throws SQLException {
        XmlEntry xmlEntry = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SELECT_BY_FILE_NAME);
            preparedStatement.setString(1, fileName);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                xmlEntry = new XmlEntry();
                xmlEntry.setId(rs.getInt("id"));
                xmlEntry.setContent(rs.getString("content"));
                xmlEntry.setCreationDate(rs.getTimestamp("creationDate"));
                xmlEntry.setFileName(fileName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rs.close();
            preparedStatement.close();
        }
        return xmlEntry;
    }*/

    /*public void createTable() throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(CREATE_TABLE);
    }*/

    /*private static List<XmlEntry> selectWithResulSet(ResultSet rs) throws SQLException, IOException {
        List<XmlEntry> xmlEntryList = new ArrayList<XmlEntry>();
        XmlEntry xmlEntry = null;
        while(rs.next()){
            xmlEntry = new XmlEntry();
            xmlEntry.setId(rs.getInt("id"));
            xmlEntry.setContent(rs.getString("content"));
            xmlEntry.setCreationDate(rs.getTimestamp("creationDate"));
            xmlEntry.setFileName(rs.getString("fileName"));
            xmlEntryList.add(xmlEntry);
        }
        return xmlEntryList;
    }*/

}
