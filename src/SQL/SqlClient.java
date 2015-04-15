package SQL;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;



public class SqlClient implements SqlClientInterface{
    
    
    private Connection conn = null;
    private Statement stmt = null;    
    public SqlClient()
    {
	// super();
	// System.out.println("Accesing SqlClient");
    }

    @Override
    public String createConnection()
    {
	// System.out.println("Validate db connection");
	String err = "";
        if (conn != null)
	{
	    System.err.println("Connection already created");
	    return err;
	}
	
	try
        {
            Class.forName(SQLNames.dbClientDriver).newInstance();
            //Get a connection
	    
	    conn = DriverManager.getConnection(SQLNames.db, 
						SQLNames.databaseUser,
						SQLNames.databasePassword);
        }
	
	catch (ClassNotFoundException except)
        {
            except.printStackTrace(System.out);
	    err = "ClassNotFoundException; Error creating SQL connection";
	    err += "\n"+String.format("DB: %s",SQLNames.db);
	    System.err.println(err);
	    return err;
        } 
	catch (InstantiationException ex) {
            // except.printStackTrace(System.out);
	    return err;
	} 
	catch (IllegalAccessException ex) {
	    err = "IllegalAccessException; Error creating SQL connection";
	    System.err.println(err);
	    return err;
	} 
	catch (SQLException ex) 
	{
	    err = "SQLException; Error creating SQL connection";
	    err += "\n"+String.format("DB: %s",SQLNames.db);
	    System.err.println(err);
	    System.err.println(ex.getMessage());
	    return err;
	}
	return err;
    }
    
    @Override
    public String insertMessage(SQLMessageRecord record)
    {
        if (conn == null)
	{
	    System.out.println("No connection");
	    return "No connection";
	}
	try
        {
	    record.setSENDTIME();

            stmt = conn.createStatement();

	    String s = record.GetSqlInsert();
	    System.err.println("SQL: "+s);

            stmt.execute( s );
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
	    String sError = sqlExcept.getMessage();
            sqlExcept.printStackTrace(System.out);
	    return sError;
        }
	return "";
    }
    
@Override
    public String insertProfile(SQLProfileRecord record)
    {
        if (conn == null)
	{
	    System.out.println("No connection");
	    return "No connection";
	}
	try
        {
            stmt = conn.createStatement();

	    String s = record.GetSqlInsert();

            System.err.println("SQL QUERY:" + s);
            stmt.execute( s );
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
	    String sError = sqlExcept.getMessage();
            sqlExcept.printStackTrace(System.out);
	    return sError;
        }
	return "";
    }

    // @Override
    @Override
    public ArrayList<Object> selectMessagesAsList(String tableName, String where, int limit)
    {
	// where clause: optional
	// must be valid SQL
	//
	// limit: set the number of records to return.  0 for all that
	// match
	
	ArrayList<Object> aList = new ArrayList<>();
        try
        {
            stmt = conn.createStatement();
	    
	    if (limit>0)
		stmt.setMaxRows(limit);
	    
	    String selectStatement = "select * from " + tableName;
	    if (where.length() > 0)
	    {
		selectStatement += " " + where;
	    }
	    
	    System.err.println("SQL QUERY: "+selectStatement);
	    
	    try	    (
			ResultSet results = 
			    stmt.executeQuery(selectStatement)
		    ) 
	    {
		ResultSetMetaData rsmd = results.getMetaData();
		// int numberCols = rsmd.getColumnCount();

		while(results.next())
		{
		    SQLMessageRecord record = SQLMessageRecord.create_FromMessageRecord(results);
		    aList.add(record);
			    
		}
	    }
            stmt.close();
	    
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace(System.out);
	    ArrayList<Object> errList = new ArrayList<>();
	    errList.add(sqlExcept.getMessage());
	    return errList;
        }
	
	return aList;
	
    }

    // @Override
    @Override
    public ArrayList<Object> selectProfilesAsList(String tableName, String where, int limit)
    {
	// where clause: optional
	// must be valid SQL
	//
	// limit: set the number of records to return.  0 for all that
	// match
	
	ArrayList<Object> aList = new ArrayList<>();
        try
        {
            stmt = conn.createStatement();
	    
	    if (limit>0)
		stmt.setMaxRows(limit);
	    
	    String selectStatement = "select * from " + tableName;
	    if (where.length() > 0)
	    {
		selectStatement += " " + where;
	    }
	    
	    System.err.println("SQL QUERY: "+selectStatement);
	    
	    try	    (
			ResultSet results = 
			    stmt.executeQuery(selectStatement)
		    ) 
	    {
		ResultSetMetaData rsmd = results.getMetaData();
		// int numberCols = rsmd.getColumnCount();

		while(results.next())
		{
		    SQLProfileRecord record = SQLProfileRecord.create_fromResultSet(results);
		    aList.add(record);
		}
	    }
            stmt.close();
	    
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace(System.out);
	    ArrayList<Object> errList = new ArrayList<>();
	    errList.add(sqlExcept.getMessage());
	    return errList;
        }
	
	return aList;
	
    }
    
    
    @Override
    public void selectAndPrint(String tableName, String where, int limit)
    {
        try
        {
            stmt = conn.createStatement();
	    String selectStatement = "select * from " + tableName;
	    if (where.length() > 0)
	    {
		selectStatement += " " + where;
	    }
	    
	    if (limit > 0)
		stmt.setMaxRows(limit); 
	    
            System.err.println("SQL QUERY:" + selectStatement);
	    try	    (
			ResultSet results = 
			    stmt.executeQuery(selectStatement)
		    ) 
	    {
		ResultSetMetaData rsmd = results.getMetaData();
		// int numberCols = rsmd.getColumnCount();
		for (int i=1; i<=2; i++)
		{
		    //print Column Names
		    System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
		}

		System.out.println("\n-------------------------------------------------");

		while(results.next())
		{
		    String imessage = results.getString("MSG");
		    String iid = results.getString("ID");
		    System.out.println(iid + "\t\t" + imessage + "\t\t");
		}
	    }
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace(System.out);
        }
    }
    
    @Override
    public void shutdown()
    {
        try
        {
            if (stmt != null)
            {
                stmt.close();
            }
            if (conn != null)
            {
                DriverManager.getConnection(SQLNames.db + ";shutdown=true");
                conn.close();
            }           
        }
        catch (SQLException sqlExcept)
        {
	    // meh
            
        }

    }

    void execute(String s) throws SQLException 
    {
	  stmt = conn.createStatement();
            System.err.println("execute SQL QUERY:" + s);
            stmt.execute( s );
            stmt.close();
    }

    String replaceProfile(int RecordID, SQLProfileRecord record) 
    {
        if (conn == null)
	{
	    System.out.println("No connection");
	    return "No connection";
	}
	try
        {
            stmt = conn.createStatement();

	    String s = record.GetSqlReplace(RecordID);
	    System.out.println(s);

            stmt.execute( s );
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
	    String sError = sqlExcept.getMessage();
            sqlExcept.printStackTrace(System.out);
	    return sError;
        }
	return "";
    }

    String insertTransaction(SQLTransactionRecord record) {
        if (conn == null)
	{
	    System.out.println("No connection");
	    return "No connection";
	}
	try
        {
	    record.setSENDTIME();

            stmt = conn.createStatement();

	    String s = record.GetSqlInsert();
	    System.err.println("SQL: "+s);

            stmt.execute( s );
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
	    String sError = sqlExcept.getMessage();
            sqlExcept.printStackTrace(System.out);
	    return sError;
        }
	return "";
    }
}