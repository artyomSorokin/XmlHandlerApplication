package sorokin.dao.tx;

import sorokin.dao.connection.ConnectionFactory;

import javax.sql.DataSource;
import java.util.concurrent.Callable;

public interface TransactionManager extends DataSource {

    public <T> T doInTransaction(Callable<T> unitOfWork) throws Exception;

    public void setConnectionFactory(ConnectionFactory connectionFactory);

}
