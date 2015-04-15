package Bankapp8;


import SQL.SQLMessageRecord;
import SQL.SQLProfileRecord;
import SQL.SQLTransactionRecord;
import SQL.SQLDatabase;
import java.util.ArrayList;
import java.util.Scanner;

public class Bankapp2 {

    private final Scanner scanner = new Scanner(System.in);
  

    public static void main2(String[] args) {
	// TODO code application logic here
	
	Bankapp2 bapp = new Bankapp2();
	bapp.main(args);
    }
    
    public void main(String[] args) 
    {
	
	// System.out.println("RpcClient?");
	

	System.out.println("Security disabled for demo");
	System.out.println("Console logged in with admin access");
	
	    //System.out.println("Enter username:");
	    //username = scanner.nextLine();
	    //System.out.println("Enter password:");
	    //password = scanner.nextLine();
	
	    mainloop: while (true)
	    {
		
		
		String msg = 
			  "  0 quit\n"
			+ "  12 create transaction, \n"
			+ "  22 outbox, \n"
			+ "  33 inbox, \n"
			+ "  14 create profile\n"
			+ "  15 list profiles\n"
			+ "  16 find profile\n"
			+ "  17 verify login/password\n"
			+ " 90 create tables\n?";
		int which;
		which = scanlineInteger(msg);
		
		if (which == 0)
		{
		    return;
		}
		else if (which < -1)
		{
		    
		    continue mainloop;
		}
		

		
		handleUserRequest(which);
		

		
		scanlineInteger("Type 1 to continue");
	    }
    
    }
    
    public void handleUserRequest(int which)  
    { 
	char ch;
	String ask;
	SQLDatabase db;
	SQLMessageRecord messageRecord;
	SQLProfileRecord profileRecord;
	SQLTransactionRecord transactionRecord;
	
	handlechoice:
	switch (which) 
	{
	    case 0:
		return;
		

	    case 11:
		{
		    messageRecord = askUserForInputForMsg(scanner);
		    sendMessageWithSQL(messageRecord);
		}
		break handlechoice;
		
	    case 12:
	    {
		transactionRecord = askUserForInputForTransaction(scanner);
		sendTransactionWithSQL(transactionRecord);
		break;
		
	    }

	case 22: 
		{
		    String username = this.promptForUsername("Get for which username?  ");

		    db = new SQLDatabase();
		    displayMessagesFrom(db, username);
		}
		break handlechoice;
	
	    case 33: 
		{
		    String username = this.promptForUsername("Get for which username?  ");
		    db = new SQLDatabase();
		    displayMessagesTo(db, username);
		}
		break handlechoice;	

		
	    case 14:
		{
		    profileRecord = askUserForInputForProfile(scanner);
		    String result = Bankapp2.saveProfile(profileRecord);
		    String s = "update ok?" + result;
		    System.out.println(s);
		    
		}
		break handlechoice;
		
	    case 15: 
		{
		    displayProfiles("");
		}
		break handlechoice;
	    case 16: 
		{
		    String username = this.promptForUsername("Get for which username?  ");
		    displayProfiles(username);
		}
		break handlechoice;
	
	    case 17:
		{
		    String email = this.promptForUsername("Verify account for email?  ");
		    String password = this.promptForUsername(email+"'s Password?  ");
		    boolean ok = validateLogin(email, password);
		    System.out.println("Validated:" +ok);
		}
		break;
		
	    case 90:
		{
		    createMsgDB();
		    createProfileDB();
		    createTransactionsDB();
		}
		break;		
		
	} // end switch
    }
    
    String createProfileDB()
    {
	String s = SQLProfileRecord.getCreateCommand();
	SQLDatabase db = new SQLDatabase();
	System.out.println(s);
	String err = db.execute(s);

	if (err.length()>0) // error
	{
	    System.out.println(err);
	}
	else
	{
	    
	    System.out.println(SQLProfileRecord.getTableName()+" OK");
	}
	return err;
    }
    
    String createMsgDB()
    {
	String s = SQLMessageRecord.getCreateCommand();
	SQLDatabase db = new SQLDatabase();
	System.out.println(s);
	String err = db.execute(s);
	
	if (err.length()>0) // error
	{
	    System.out.println(err);
	}
	else
	{
	    System.out.println(SQLMessageRecord.getTableName()+" OK");
	}
	return err;

    }
    
    String createTransactionsDB()
    {
	String s = SQLTransactionRecord.getCreateCommand();
	SQLDatabase db = new SQLDatabase();
	System.out.println(s);
	String err = db.execute(s);
	
	if (err.length()>0) // error
	{
	    System.out.println(err);
	}
	else
	{
	    System.out.println(SQLTransactionRecord.getTableName()+" OK");
	}
	return err;

    }
    

    void displayMessagesFrom(SQLDatabase db, String sFrom) 
    {
	    // RpcClient rpc;
	    // rpc = new RpcClient(); 
	    
	    int howmany = scanlineInteger("How many records to display?");
	
	    ArrayList<Object> alist;
	    
	    // RPC call
	    int limit = 0;
	    
	    alist = db.GetMessagesFrom(sFrom, limit);
	    
	    ArrayList<SQLMessageRecord> messageRecords;
	    
	    messageRecords = SQLMessageRecord.convertFromObjects(alist);
	    String s = SQLMessageRecord.GetStringFromRecordList(messageRecords);

	    System.out.println("SQLGetRecordsFromSender returned "+s);
	
	
    }    
    
    
    
    void displayMessagesTo(SQLDatabase db, String sTo) 
    {
	
	ArrayList<Object> alist;
	    
	// RPC call

	int limit = 0;
	alist =	db.getMessagesTo( sTo, limit);

	ArrayList<SQLMessageRecord> messageRecords;
	messageRecords = SQLMessageRecord.convertFromObjects(alist);
	String s = SQLMessageRecord.GetStringFromRecordList(messageRecords);

	System.out.println("SQLGetRecordsFromSender returned "+s);
    }

    void displayProfiles(String sID) 
    {
	SQLDatabase sqlDB = new SQLDatabase();
	
	ArrayList<Object> alist;
	    
	// RPC call

	int limit = 0;
	alist =	sqlDB.getProfiles( sID, limit);
	ArrayList<SQLProfileRecord> profileRecords;
	profileRecords = SQLProfileRecord.convertFromObjects(alist);
	String s = SQLProfileRecord.GetStringFromRecordList(profileRecords);
	System.out.println("rpc.displayProfiles returned\n"+s);
    }
    
    static public boolean validateLogin(String sID, String password) 
    {
	SQLDatabase sqlDB = new SQLDatabase();
	
	ArrayList<Object> alist;
	    
	// RPC call

	int limit = 1;
	alist =	sqlDB.getProfiles( sID, limit);
	if (alist.size() > 0)
	{
	    ArrayList<SQLProfileRecord> profileRecords;
	    profileRecords = SQLProfileRecord.convertFromObjects(alist);
	    String s = SQLProfileRecord.GetStringFromRecordList(profileRecords);
	    System.out.println("rpc.displayProfiles returned\n"+s);

	    for (SQLProfileRecord profile: profileRecords)
	    {
		if (profile.getEMAIL().equalsIgnoreCase(sID))
		{
		    if (profile.getPASSWORD().equalsIgnoreCase(password))
		    return true;
		}
	    }
	}
	return false;
    }

    static public SQLProfileRecord getProfile(String sID, String password) 
    {
	SQLDatabase sqlDB = new SQLDatabase();
	
	ArrayList<Object> alist;
	    
	// RPC call

	int limit = 1;
	alist =	sqlDB.getProfiles( sID, limit);
	if (alist.size() > 0)
	{
	    ArrayList<SQLProfileRecord> profileRecords = 
			SQLProfileRecord.convertFromObjects(alist);
	    
	    String s = SQLProfileRecord.GetStringFromRecordList(profileRecords);
	    System.out.println("rpc.displayProfiles returned\n"+s);
	
	    for (SQLProfileRecord profile: profileRecords)
	    {
		if (profile.getEMAIL().equalsIgnoreCase(sID))
		{
		    if (profile.getPASSWORD().equalsIgnoreCase(password))
		    {
			// we have a match
			return profile;
		    }
		}
	    }
	}
	
	SQLProfileRecord profile = new SQLProfileRecord();
	return profile;
    }

    static public String saveProfile(SQLProfileRecord record) 
    {
	String result = "";
	SQLDatabase sqlDB = new SQLDatabase();
	ArrayList<Object> alist;
	// RPC call
	String sID = record.getEMAIL();
	int limit = 1;
	
	// get record list
	alist =	sqlDB.getProfiles( sID, limit);
	
	if (alist.size() > 0)
	{
	    // convert to profilerecords
	    ArrayList<SQLProfileRecord> profileRecords;
	    profileRecords = SQLProfileRecord.convertFromObjects(alist);
	    int RecordID = -1;

	    // process records
	    for (SQLProfileRecord profile: profileRecords)
	    {
		if (profile.getEMAIL().equalsIgnoreCase(sID))
		{
		    RecordID = profile.getID();
		    break;
		}
	    }

	    if (RecordID > 0)
	    {
		result += "replacing record "+RecordID;
		result += sqlDB.replaceProfile(RecordID,record);
	    }
	}
	else
	{
	    result += "inserting new record";
	    result = sqlDB.insertProfile(record);
	}
	
	return result;
    }
    
    
    private SQLMessageRecord askUserForInputForMsg(Scanner scanner) {
	
	System.out.print("Sender:    ");
	String sSender = scanner.nextLine();
	System.out.print("Recipient: ");
	String sRecipient = scanner.nextLine();
	System.out.print("Subject:   ");
	String sSubject = scanner.nextLine();
	System.out.print("Message:   ");
	String sMessage = scanner.nextLine();
	
	SQLMessageRecord record;
	record = new SQLMessageRecord();
	record.setSENDER(sSender);
	record.setRECEIVER(sRecipient);
	record.setSUBJECT(sSubject);
	record.setMESSAGE(sMessage);
	return record;
    }

    private SQLProfileRecord askUserForInputForProfile(Scanner scanner) 
    {
	int id = 0;
	
	System.out.printf("%20s:",SQLProfileRecord.EMAIL_F);
	String sEMAIL = scanner.nextLine();

	System.out.printf("%20s:",SQLProfileRecord.PASSWORD_F);
	String sPASSWORD = scanner.nextLine();

	System.out.printf("%20s:",SQLProfileRecord.FIRSTNAME_F);
	String sFIRSTNAME = scanner.nextLine();
	
	System.out.printf("%20s:",SQLProfileRecord.LASTNAME_F);
	String sLASTNAME = scanner.nextLine();
	
	System.out.printf("%20s:",SQLProfileRecord.PHONENUMBER_F);
	String sPHONENUMBER = scanner.nextLine();
	
	String sADDRESS="!@#";
	int iBalance = 100;
	SQLProfileRecord record;
	record = SQLProfileRecord.create_FromParameters
		(
			    id,
			    sEMAIL, // should be username but I was using email as their user name 
			    sPASSWORD, 
			    sFIRSTNAME, 
			    sLASTNAME,
			    sEMAIL,
			    sADDRESS,
			    iBalance,
			    sPHONENUMBER
		); 		
	
/*int iId, 
					    String sUSERNAME, 
					    String sPASSWORD, 
					    String sFIRSTNAME, 
					    String sLASTNAME, 
					    String sEMAIL,
					    String sADDRESS,
					    int	   iBALANCE,
					    String sPHONENUMBER 	
*/	
	return record;
    }
    
    public String promptForUsername(String s) 
    {
	System.out.print(s);
	String username = scanner.nextLine();
	return username;
    }

    public int scanlineInteger(String msg) 
    {
	System.out.print(msg+": ");
	int which;
	String s=""; 
	try
	{
	    s = scanner.nextLine(); 
	    which = Integer.parseInt(s);
	}
	catch (Exception ex)
	{
	    System.err.printf("Input (%s) not a valid integer\n",s);
	    which = -1;
	}
	return which;
    }


    public static String sendMessageWithSQL(SQLMessageRecord messageRecord) {
	SQLDatabase db;
	db = new SQLDatabase();
	String result = db.insertMessage(messageRecord);
	String s = "post ok?" + result;
	return result;
    }

    private SQLTransactionRecord askUserForInputForTransaction(Scanner scanner) {
	System.out.print("Sender:    ");
	String sSender = scanner.nextLine();
	System.out.print("Recipient: ");
	String sRecipient = scanner.nextLine();
	System.out.print("Amount:   ");
	String sAmount = scanner.nextLine();
	
	// fix me because parseDouble can throw exceptions
	double dAmount = Double.parseDouble(sAmount);
	
	SQLTransactionRecord record;
	record = new SQLTransactionRecord();
	record.setSENDER(sSender);
	record.setRECEIVER(sRecipient);
	record.setAmount(dAmount);
	return record;
    }

    private String sendTransactionWithSQL(SQLTransactionRecord transactionRecord) {
	SQLDatabase db;
	db = new SQLDatabase();
	String result = db.insertTransaction(transactionRecord);
	String s = "post ok?" + result;
	return result;
    }

}

