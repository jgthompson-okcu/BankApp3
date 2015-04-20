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
    // create_fromResultSet(ResultSet results)
    // toString
    // GetSqlInsert
    // GetSqlReplace  (actually we don't have a replace method yet.  could
    //                 be done like the one in the SQLProfileRecord class.
    
    private static final String TABLENAME = "TRANSACTIONS";

    private int                   ID = 0;
    private static final String   ID_TYPE = "ID INTEGER NOT NULL";
    private static final String   ID_F = "ID";

    private String                SENDER = "DEFAULTSENDER";    
    public static final String    SENDER_F = "SENDER";
    private static final String   SENDER_TYPE = SENDER_F + " VARCHAR(20) NOT NULL";    

    private String                RECEIVER = "DEFAULTRECIEVER";  
    public static final String    RECEIVER_F = "RECEIVER";
    private static final String   RECEIVER_TYPE = RECEIVER_F + " VARCHAR(20) NOT NULL";

    private long                  SENDTIME = 0;         // timestamp
    public static final String    SENDTIME_F = "SENDTIME"; 
    private static final String   SENDTIME_TYPE = SENDTIME_F+" DECIMAL(15) NOT NULL"; 
    
    private String                PROCESSED = "PENDING";    
    public static final String    PROCESSED_F = "PROCESSED";
    private static final String   PROCESSED_TYPE = PROCESSED_F + " VARCHAR(10) NOT NULL"; 
    
    private double		  AMOUNT = 1;   
    public static final String    AMOUNT_F = "AMOUNT";  
    private static final String   AMOUNT_TYPE = AMOUNT_F+" INTEGER NOT NULL";

    public static final String getTableName() 
    {
	String s = SQLNames.sqlDB_SchemeName + "." + TABLENAME;
	return s;
    }    
    
    
    @Override
    public String getFieldNames()
    {
            String fieldnames = 
            
            SENDER_F   +	", " + 
            RECEIVER_F +	", " + 
            SENDTIME_F +	", " +  
	    PROCESSED_F +	", " +  
	    AMOUNT_F   +	" ";   // last one gets no comma
            return fieldnames;
    }
    
    static public SQLTransactionRecord create_FromResultSet(ResultSet results) throws SQLException 
    {
	SQLTransactionRecord record;

	int iId =	    results.getInt(ID_F);
	String sSender =    results.getString(SENDER_F);
	String sReceiver =  results.getString(RECEIVER_F);
	long lSendtime =    results.getLong(SENDTIME_F);
	String sProcessed = results.getString(PROCESSED_F);
	long lAmount =	    results.getLong(AMOUNT_F);
	
	
	record = create_FromParameters(
		iId,
		sSender,
		sReceiver,
		lSendtime,
		sProcessed,
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
	    s+= rec.toString()+"\n";
	}
	return s;
    }
    
    
    public static SQLTransactionRecord create_FromParameters( 
		    int iId, 
		    String sSender,
		    String sReceiver,
		    long lSendtime,
		    String sProcessed,
		    double dAmount
	    )
	    
    {
	SQLTransactionRecord thisRecord = new SQLTransactionRecord();
	
	thisRecord.ID = iId;
	thisRecord.SENDER = sSender;
	thisRecord.RECEIVER = sReceiver;
	thisRecord.SENDTIME = lSendtime;
	thisRecord.PROCESSED = sProcessed;
	thisRecord.AMOUNT = dAmount;
	return thisRecord;
	
    }
    
    @Override
    public String toString() {
	SQLTransactionRecord record = this;
	return toString(record);

    }

    public static String toString(SQLTransactionRecord record) {
	String s;
	    s = String.format(
			  ID_F +		":%-5d "     // 0 ID
			+ SENDER_F+		":%-10s "    // 1 SENDER
			+ RECEIVER_F+		":%-10s "    // 2 RECEIVER
			+ SENDTIME_F+		":%-8s "	     // 3 SENDTIME
			+ PROCESSED_F+		":%-10s "     // 5 PROCESSED
			+ AMOUNT_F +		":%-10.2f ",  // 6 AMOUNT

			record.ID,			    // 0 ID
			record.SENDER,			    // 1 SENDER
			record.RECEIVER,		    // 2 RECEIVER
			record.getSENDTIMESTRING(),	    // 3 SENDTIME
			record.PROCESSED,		    // 5 PROCESSED
			(double) (record.AMOUNT / 100.00)   // 6 AMOUNT
	);
	return s;

    }
        
    @Override
    public String GetSqlInsert()
    {
	
	String s;
	this.setSENDTIME();
	s = String.format("INSERT INTO %s (%s) VALUES "+
		"("
		+ "'%s', " // strings need single quotes
		+ "'%s', " // strings need single quotes
		+ " %d , " // sendtime
		+ "'%s', "  // strings need single quotes
		+ " %d  "  // last one no comma // we have long / double confusion
		+ ")",
		getTableName(),
		this.getFieldNames(),
		this.SENDER,	//s SENDER
		this.RECEIVER,	//s RECEIVER
		this.SENDTIME,	//d SENDTIME
		this.PROCESSED,	//s "PROCESSED, PENDING or FAILED"
		(int) (100 * this.AMOUNT)	//d AMOUNT   // last one no comma
	);
	// System.err.println("SQL:"+s);
	return s;
    }

    public int getID() {
	return ID;
    }

    public String getSENDER() {
	return SENDER;
    }

    public void setSENDER(String SENDER) {
	this.SENDER = SENDER;
    }

    public String getRECEIVER() {
	return RECEIVER;
    }

    public void setRECEIVER(String RECEIVER) {
	this.RECEIVER = RECEIVER;
    }

    public long getSENDTIME() {
	return SENDTIME;
    }

    public void setSENDTIME(long SENDTIME) {
	this.SENDTIME = SENDTIME;
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
		    SENDER_TYPE		+ comma + newlinetab + 
		    RECEIVER_TYPE	+ comma + newlinetab +
		    SENDTIME_TYPE	+ comma + newlinetab +
		    PROCESSED_TYPE	+ comma + newlinetab +
		    AMOUNT_TYPE		+	  newlinetab + // last one no comma
		    ")";
	return s;
    }

    public double getAmount() {
	return Double.parseDouble(String.format("%.2f",this.AMOUNT));
    }

    public void setAmount(double amount) {
	this.AMOUNT = Double.parseDouble(String.format("%.2f",amount));
    }
    
    public void setAmount(String amount) 
    {
    }

    public String getPROCESSED() {
	validProcessingType(PROCESSED);
	return PROCESSED;
    }
    
    private static final String processing_modes = "PENDING FAILED POSTED";
    public static boolean validProcessingType(String s)
    {
	boolean ok = (processing_modes.contains(s));
	if (ok)
	    return ok;
	String err = String.format("Error - PROCESSED value (%s) invalid - must be %s", s, processing_modes);
	System.err.println(err);
	throw new RuntimeException (err);
    }

    public void setPROCESSED(String PROCESSED) {
	validProcessingType(PROCESSED);
	this.PROCESSED = PROCESSED;
    }
    @Override
    public String tableName() {
	return TABLENAME;
    }

    
}
