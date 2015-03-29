package SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLMessageRecord implements SqlRecordInterface
{
    private static String TABLENAME = "MESSAGES";
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

    private int                   part = 1;   
    public static final String    PART_F = "PART";  
    private static final String   PART_TYPE = PART_F+" DECIMAL(5) NOT NULL";
    
    private String                subject = "DEFAULTSUBJECT";
    public static final String    SUBJECT_F = "SUBJECT";    
    private static final String   SUBJECT_TYPE = SUBJECT_F +" VARCHAR(100)";   

    private String                message = "DEFAULTMESSAGE";
    public static final String    MESSAGE_F = "MESSAGE";
    private static final String   MESSAGE_TYPE = MESSAGE_F + " VARCHAR(256) NOT NULL";   
    
    private int                   messageseen = 0;
    public static final String    MESSAGESEEN_F = "MESSAGESEEN";
    private static final String   MESSAGESEEN_TYPE = MESSAGESEEN_F+" DECIMAL(1) NOT NULL";

    public static String getTableName() 
    {
	String s = SQLNames.sqlDB_SchemeName + "." + TABLENAME;
	return s;
    }    
    
    @Override
    public String getFieldNames()
    {
            String fieldnames = 
            
            SENDER_F +   ", " + 
            RECEIVER_F + ", " + 
            SENDTIME_F + ", " + 
            PART_F +     ", " + 
            SUBJECT_F +  ", " + 
            MESSAGE_F +  ", " + 
            MESSAGESEEN_F;       // last one gets no comma
            return fieldnames;
    }
    
    static public SQLMessageRecord create_FromMessageRecord(ResultSet results) throws SQLException 
    {
	SQLMessageRecord record;

	int iId =	    results.getInt(ID_F);
	String sSender =    results.getString(SENDER_F);
	String sReceiver =  results.getString(RECEIVER_F);
	long lSendtime =    results.getLong(SENDTIME_F);
	int iPart =	    results.getInt(PART_F);
	String sSubject =   results.getString(SUBJECT_F);
	String sMessage =   results.getString(MESSAGE_F);
	int iMessageSeen =  results.getInt(MESSAGESEEN_F);
	
	record = create_FromParameters(
		iId,
		sSender,
		sReceiver,
		lSendtime,
		iPart,
		sSubject,
		sMessage,
		iMessageSeen);
	
	return record;
    }
    
    public static ArrayList<SQLMessageRecord> convertFromObjects(ArrayList<Object> alist) 
    {
	ArrayList<SQLMessageRecord> blist = new ArrayList();
	
	for (Object a: alist)
	{
	    blist.add((SQLMessageRecord) a);
	}
	return blist;
    }

     public static String GetStringFromRecordList(ArrayList<SQLMessageRecord> records) {
	String s = "";
	for (SQLMessageRecord rec: records)
	{
	    s+= rec+"\n";
	}
	return s;
    }
    
    
    public static SQLMessageRecord create_FromParameters( 
		    int iId, 
		    String sSender,
		    String sReceiver,
		    long lSendtime, 
		    int iPart,
		    String sSubject, 
		    String sMessage,
		    int iMessageSeen
	    )
	    
    {
	SQLMessageRecord thisRecord = new SQLMessageRecord();
	
	thisRecord.ID = iId;
	thisRecord.sender = sSender;
	thisRecord.receiver = sReceiver;
	thisRecord.sendtime = lSendtime; 
	thisRecord.part = iPart;
	thisRecord.subject = sSubject;
	thisRecord.message = sMessage;
	thisRecord.messageseen = iMessageSeen;
	return thisRecord;
	
    }
    
    @Override
    public String toString() {
	SQLMessageRecord record = this;
	return toString(record);

    }

    public static String toString(SQLMessageRecord record) {
	String s = String.format(
                  ID_F +         ":%-5d "
		+ SENDER_F+     ":%-10s "
		+ RECEIVER_F+   ":%-10s "
		+ SENDTIME_F+   ":%8s "
		+ PART_F+       ":%-1d "
		+ SUBJECT_F+    ":%-20s "
		+ MESSAGE_F+    ":%-40s "
		+ MESSAGESEEN_F+":%-1d ",
		record.ID,
		record.sender,
		record.receiver,
		record.getSENDTIMESTRING(),
		record.part,
		record.subject,
		record.message,
		record.messageseen);
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
		+ " %d , "
		+ "'%s', "
		+ "'%s', "
		+ " %d   "  // last one no comma
		+ ")",
		    getTableName(), 
		    this.getFieldNames(),
		    this.sender,	//s sender
		    this.receiver,	//s receiver
		    this.sendtime,	//d sendtime
		    this.part,		//d part
		    this.subject,	//s subject		    
		    this.message,	//s message
		    this.messageseen	//d messageseen
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

    public int getPART() {
	return part;
    }

    public void setPART(int PART) {
	this.part = PART;
    }

    public String getSUBJECT() {
	return subject;
    }

    public void setSUBJECT(String SUBJECT) {
	this.subject = SUBJECT;
    }

    public String getMESSAGE() {
	return message;
    }

    public void setMESSAGE(String MESSAGE) {
	this.message = MESSAGE;
    }

    public int getMESSAGESEEN() {
	return messageseen;
    }

    public void setMESSAGESEEN(int MESSAGESEEN) {
	this.messageseen = MESSAGESEEN;
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
			    PART_TYPE     + comma + newlinetab + 
			    SUBJECT_TYPE  + comma + newlinetab + 
			    MESSAGE_TYPE  + comma + newlinetab + 
			    MESSAGESEEN_TYPE+       newlinetab + // no comma on last item
			    ")";
		return s;

    }
    
}
