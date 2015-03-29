package SQL;


import java.io.Serializable;
import java.util.ArrayList;


/**
 *
 * @author Jeff
 */

public interface SqlClientInterface extends Serializable {

    String createConnection();

    String insertMessage(SQLMessageRecord record);
    String insertProfile(SQLProfileRecord record);

    void selectAndPrint(String tableName, String where, int limit);

    ArrayList<Object> selectMessagesAsList(String tableName, String where, int limit);
    ArrayList<Object> selectProfilesAsList(String tableName, String where, int limit);

    void shutdown();
    
}
