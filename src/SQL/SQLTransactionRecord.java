package SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLTransactionRecord implements SqlRecordInterface
{
    // member variables, and the information used to record
    // their values in the sql table
    //
    // _F    is the fieldname for that variable.
    // _TYPE is the data type used when
    //       creating that variable.
    //
    // NOTE!  WHEN YOU ADD OR REMOVE OR CHANGE THE NAME OF FIELDS, YOU
    // MUST CHANGE THE FOLLOWING METHODS!
    //
    // getFieldNames 
    // getUpdateFieldList
    // create_FromParameters
    // create_fromMessageRecord(ResultSet results)
    // toString
    // GetSqlInsert
    // GetSqlReplace  (actually we don't have a replace method yet.  could
    //                 be done like the one in the SQLProfileRecord class.
    
    private static final String TABLENAME = "TRANSACTIONS";
    
    private int                   ID = 0;
    private static final String   ID_TYPE = "ID INTEGER NOT NULL";
    private static final String   ID_F = "ID";

    private String                sender = "DEFAULTSENDER";    
    public static final String    SENDER_F = "SENDER";
    private static final String   SENDER_TYPE = SENDER_F + " VARCHAR(20) NOT NULL";    

    private String                receiver = "DEFAULTRECIEVER";  
    public static final String    RECEIVER_F = "RECEIVER";
    private static final String   RECEIVER_TYPE = RECEIVER_F + " VARCHAR(20) NOT NULL";

    private long                  sendtime = 0;         // timestamp
    public static final String    SENDTIME_F = "SENDTIME"; 
    private static final String   SENDTIME_TYPE = SENDTIME_F+" DECIMAL(15) NOT NULL"; 

    private double                amount = 1;   
    public static final String    AMOUNT_F = "AMOUNT";  
    private static final String   AMOUNT_TYPE = AMOUNT_F+" DECIMAL(5) NOT NULL";

    public static final String getTableName() 
    {
	String s = SQLNames.sqlDB_SchemeName + "." + TABLENAME;
	return s;
    }    
    
    
    @Override
    public String getFieldNames()
    {
            String fieldnames = 
            
            SENDER_F   +   ", " + 
            RECEIVER_F + ", " + 
            SENDTIME_F + ", " +  
	    AMOUNT_F   + " ";   // last one gets no comma
            return fieldnames;
    }
    
    static public SQLTransactionRecord create_FromMessageRecord(ResultSet results) throws SQLException 
    {
	SQLTransactionRecord record;

	int iId =	    results.getInt(ID_F);
	String sSender =    results.getString(SENDER_F);
	String sReceiver =  results.getString(RECEIVER_F);
	long lSendtime =    results.getLong(SENDTIME_F);
	long lAmount =	    results.getLong(AMOUNT_F);
	
	record = create_FromParameters(
		iId,
		sSender,
		sReceiver,
		lSendtime,
		lAmount
	    );
	
	return record;
    }
    
    public static ArrayList<SQLTransactionRecord> convertFromObjects(ArrayList<Object> alist) 
    {
	ArrayList<SQLTransactionRecord> blist = new ArrayList();
	
	for (Object a: alist)
	{
	    blist.add((SQLTransactionRecord) a);
	}
	return blist;
    }

     public static String GetStringFromRecordList(ArrayList<SQLTransactionRecord> records) {
	String s = "";
	for (SQLTransactionRecord rec: records)
	{
	    s+= rec+"\n";
	}
	return s;
    }
    
    
    public static SQLTransactionRecord create_FromParameters( 
		    int iId, 
		    String sSender,
		    String sReceiver,
		    long lSendtime,
		    long lAmount
	    )
	    
    {
	SQLTransactionRecord thisRecord = new SQLTransactionRecord();
	
	thisRecord.ID = iId;
	thisRecord.sender = sSender;
	thisRecord.receiver = sReceiver;
	thisRecord.sendtime = lSendtime;
	thisRecord.amount = lAmount;
	return thisRecord;
	
    }
    
    @Override
    public String toString() {
	SQLTransactionRecord record = this;
	return toString(record);

    }

    public static String toString(SQLTransactionRecord record) {
	String s = String.format(
                  ID_F +         ":%-5d "
		+ SENDER_F+     ":%-10s "
		+ RECEIVER_F+   ":%-10s "
		+ SENDTIME_F+   ":%8s " 
		+ AMOUNT_F +    "%-10d",
		record.ID,
		record.sender,
		record.receiver,
		record.getSENDTIMESTRING(),
		record.amount
		);
	return s;

    }
        
    @Override
    public String GetSqlInsert()
    {
	
	String s = String.format("INSERT INTO %s (%s) VALUES "+
		"("
		+ "'%s', "
		+ "'%s', "
		+ " %d , "
		+ " %.0f  "  // last one no comma // we have long / double confusion
		+ ")",
		    getTableName(), 
		    this.getFieldNames(),
		    this.sender,	//s sender
		    this.receiver,	//s receiver
		    this.sendtime,	//d sendtime
		    this.amount		//d amount   // last one no comma
		
		);
	// System.err.println("SQL:"+s);
	return s;
    }

    public int getID() {
	return ID;
    }

    public String getSENDER() {
	return sender;
    }

    public void setSENDER(String SENDER) {
	this.sender = SENDER;
    }

    public String getRECEIVER() {
	return receiver;
    }

    public void setRECEIVER(String RECEIVER) {
	this.receiver = RECEIVER;
    }

    public long getSENDTIME() {
	return sendtime;
    }

    public void setSENDTIME(long SENDTIME) {
	this.sendtime = SENDTIME;
    }
    
    public String getSENDTIMESTRING()
    {
	long millis = this.getSENDTIME();
	millis -= 1000*60*60*5;
	if (millis>0)
	{
	   return Utils.getSendTimeString(millis);
	}
	else
	    return "00:00:00";
    }
    
    void setSENDTIME() 
    {
	long millis = System.currentTimeMillis();
	System.err.println(millis);
	this.setSENDTIME(millis);
	System.err.println(this.getSENDTIMESTRING());
    }

    public static String getCreateCommand() 
    {
	String newlinetab = "\n\t";
	String comma = ",";
	
	String s = "CREATE TABLE "+getTableName() + "(" + newlinetab + 
		    ID_TYPE + newlinetab +
		    "PRIMARY KEY GENERATED ALWAYS AS IDENTITY " + newlinetab +
		    "(START WITH 1, INCREMENT BY 1),"  + newlinetab +
		    SENDER_TYPE   + comma + newlinetab + 
		    RECEIVER_TYPE + comma + newlinetab +
		    SENDTIME_TYPE + comma + newlinetab +
		    AMOUNT_TYPE +	    newlinetab + // last one no comma
		    ")";
	return s;
    }

    public double getAmount() {
	return amount;
    }

    public void setAmount(double amount) {
	this.amount = amount;
    }
    
}
