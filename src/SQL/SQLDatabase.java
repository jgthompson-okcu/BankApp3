package SQL;


import java.sql.SQLException;
import java.util.ArrayList;

public class SQLDatabase 
{
    public SQLDatabase()
    {
	System.out.println("Constructor for SQLDatabase");
	// actually this is just a constructor - we could
	// comment that print statement out.
    }
    
    /**
     * Executes a given SQL statement
     * A. creates an sql connection
     * B. executes the sql command in the s parameter by calling oursql.execute(s)
     * C. shuts down the sql connection
     *
     * @param s
     * @return
     */
    public String execute(String s)
    {
	String err = "";
	SqlClient ourSQL;
	ourSQL = new SqlClient();
	ourSQL.createConnection();
	try 
	{
	    ourSQL.execute(s);
	} 
	catch (SQLException ex) 
	{
	    err = ex.getMessage();
	}
	ourSQL.shutdown();
	return err;
    }

    
    /**
     * Inserts a profile record into the profiles table.
     * A: creates sql connection
     * B: inserts  the profilerecord from the parameter into the profile table by calling oursql.insertProfile(record)
     * C: shuts down the sql connection
     *
     * @param record
     * @return
     */
    public String insertProfile(SQLProfileRecord record) 
    {
	SqlClient ourSQL;
	ourSQL = new SqlClient();
	String sErr = ourSQL.createConnection();
	if (sErr.length() == 0)
	{
	    String table = SQLMessageRecord.getTableName();
	    ourSQL.insertProfile(record);
	    ourSQL.shutdown();
	}
	return sErr;
    }


    
    /**
     * inserts a Message record into the Messages table
     * A Creates sql connection
     * B Inserts  the message record from the parameter into the profile table by calling ourSQL.insertMessage
     * C shuts down the sql connection
     * @param record
     * @return
     */
    public String insertMessage(SQLMessageRecord record) {
	SqlClient ourSQL;
	ourSQL = new SqlClient();
	String sErr = ourSQL.createConnection();
	if (sErr.length() == 0)
	{
	    String table = SQLMessageRecord.getTableName();
	    ourSQL.insertMessage(record);
	    ourSQL.shutdown();
	}
	return sErr;
    }
    
    /**
     * gets up to n message records from the messages table WHERE SENDER LIKE" sFrom.
     * A: creates connection
     * B: runs ourSQL.selectMessagesAsList()
     * C: shuts down connection

     * @param sFrom
     * @param limit
     * @return ArrayList of records
     */
    public ArrayList<Object> GetMessagesFrom(String sFrom, int limit) {
	ArrayList<Object> records = new ArrayList();
	SqlClient ourSQL = new SqlClient();
	String sErr = ourSQL.createConnection();
	if (sErr.length() == 0)
	{
	    String where = String.format("WHERE SENDER LIKE '%%%s%%'",sFrom);
	    
	    records = ourSQL.selectMessagesAsList(
		    SQLMessageRecord.getTableName(),
		    where, 
		    limit );
	    
	    ourSQL.shutdown();

	}
	return records;
    }    

    /**
     * gets message records from the messages table 'WHERE RECEIVER LIKE' sTo parameter.
     * creates connection
     * runs ourSQL.selectMessagesAsList()
     * shuts down connection
     *
     * @param sTo
     * @param limit
     * @return arraylist of records
     */
    public ArrayList<Object> getMessagesTo(String sTo, int limit) {
	ArrayList<Object> records = new ArrayList();
	SqlClient ourSQL = new SqlClient();
	boolean bok;
	String sErr = ourSQL.createConnection();
	if (sErr.length() == 0)
	{
	    String where = String.format("WHERE RECEIVER LIKE '%%%s%%'",sTo);
	    
	    records = ourSQL.selectMessagesAsList(
		    SQLMessageRecord.getTableName(),
		    where, 
		    limit);
	    
	    ourSQL.shutdown();

	}
	return records;
    }    


    
    /**
     *
     * gets up to limit profile records from the profile table 'WHERE EMAIL LIKE' sId parameter.
     * A: creates connection
     * B: runs ourSQL.selectProfilesAsList()
     * C: shuts down connection
     * @param sID
     * @param limit
     * @return arraylist of records
     */
    public ArrayList<Object> getProfiles(String sID, int limit) 
    {
	String tablename = SQLProfileRecord.getTableName();
	System.out.println("Table:"+tablename);
	ArrayList<Object> records = new ArrayList();
	SqlClient ourSQL = new SqlClient();
	boolean bok;
	String sErr = ourSQL.createConnection();
	if (sErr.length() == 0)
	{   String where = "";
	    if (sID.length()>0)
	    {
		where = String.format("WHERE EMAIL LIKE '%%%s%%'",sID);
	    }
	    records = ourSQL.selectProfilesAsList(tablename,where, limit);
	    ourSQL.shutdown();

	}
	return records;
    }



    /**
     * replaces the profile record from the profile table with a new profile record (parameter)
     * where the record id that matches the specified RecordId parameter.
     * A. creates connection
     * B. runs ourSQL.replaceProfile()
     * C. shuts down connection
     * 
     * @param RecordID
     * @param record
     * @return Error String
     */
    public String replaceProfile(int RecordID, SQLProfileRecord record) 
    {
	SqlClient ourSQL;
	ourSQL = new SqlClient();
	String sErr = ourSQL.createConnection();
	if (sErr.length() == 0)
	{
	    String table = SQLMessageRecord.getTableName();
	    ourSQL.replaceProfile(RecordID, record);
	    ourSQL.shutdown();
	}
	return sErr;
    }

    /**
     * inserts a Transaction record into the Transaction table
     * A Creates sql connection
     * B Inserts  the message record from the parameter into the profile table by calling ourSQL.insertMessage
     * C shuts down the sql connection
     * @param record
     * @return
     */
    public String insertTransaction(SQLTransactionRecord record) {
	SqlClient ourSQL;
	ourSQL = new SqlClient();
	String sErr = ourSQL.createConnection();
	if (sErr.length() == 0)
	{
	    String table = SQLTransactionRecord.getTableName();
	    ourSQL.insertTransaction(record);
	    ourSQL.shutdown();
	}
	return sErr;
    }
    
}
