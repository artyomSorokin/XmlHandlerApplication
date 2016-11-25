package sorokin.dao.tx;

import org.apache.log4j.Logger;
import sorokin.dao.connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;

public class TransactionManagerImpl extends BaseDataSource implements TransactionManager{

    private ConnectionFactory connectionFactory;
    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>();
    private Logger log = Logger.getLogger(TransactionManager.class);

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T> T doInTransaction(Callable<T> unitOfWork) throws Exception {
        log.info("Create and customize database connection");
        Connection connection = connectionFactory.newConnection();
        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        connection.setAutoCommit(false);
        log.info("Create local thread connection");
        connectionHolder.set(connection);
        try{
            log.info("Start of transaction");
            T result = unitOfWork.call();
            connection.commit();
            log.info("End of transaction");
            return result;
        } catch (Exception e){
            log.error("Error work with database");
            log.error(e.getMessage());
            connection.rollback();
            log.info("Rollback database");
            throw e;
        }
        finally {
            connection.close();
            connectionHolder.remove();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionHolder.get();
    }
}
