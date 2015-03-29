/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SQL;

/**
 *
 * @author Jeff
 */
public class SQLNames {
    public static final String database = "BANK";
    public static final String db = "jdbc:derby://localhost:1527/" + database + ";";

    public static final String databaseUser = "BANK"; // master username to the sql database
    public static final String databasePassword = "BANK"; // password for master username for sql database
    
    public static final String dbClientDriver = "org.apache.derby.jdbc.ClientDriver";
    
    // defines which schema (folder) in the database
    public static final String sqlDB_SchemeName = "BANK";
    

}
