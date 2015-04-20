package SQL;


import java.io.Serializable;
import java.util.ArrayList;


/**
 *
 * @author Jeff
 */

public interface SqlClientInterface extends Serializable {

    String createConnection();

    String insert(SQLMessageRecord record);
    String insert(SQLProfileRecord record);
    String insert(SQLTransactionRecord record);

    void selectAndPrint(String tableName, String where, int limit);

    ArrayList<Object> selectMessagesAsList(String tableName, String where, int limit);
    ArrayList<Object> selectProfilesAsList(String tableName, String where, int limit);
    ArrayList<Object> selectTransactionsAsList(String tableName, String where, int limit);

    void shutdown();
    
}
