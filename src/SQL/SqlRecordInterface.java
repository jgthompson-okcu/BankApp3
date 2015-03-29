package SQL;


import java.io.Serializable;

/**
 *
 * @author Jeff
 */
public interface SqlRecordInterface extends Serializable {
    
    String GetSqlInsert();
    @Override
    String toString();
    String getFieldNames();



    
}
