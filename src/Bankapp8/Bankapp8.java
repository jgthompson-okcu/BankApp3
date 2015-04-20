package Bankapp8;


import SQL.SQLMessageRecord;
import SQL.SQLProfileRecord;
import SQL.SQLTransactionRecord;
import SQL.SQLDatabase;
import SQL.SQLDatabase;
import SQL.SQLMessageRecord;
import SQL.SQLProfileRecord;
import SQL.SQLTransactionRecord;
import SQL.Utils;
import SQL.Utils;
import java.util.ArrayList;
import java.util.Scanner;

class LoggedInUser
{
    private SQLProfileRecord user = new SQLProfileRecord();
    public int g_UserId = -1;
    public int g_Access = -1;
    
    public LoggedInUser()
    {
	clearUser();
    }
    
    public boolean isLoggedIn()
    {
	return g_UserId >= 0;
    }   
    
    public SQLProfileRecord getUser() {
	return user;
    }

    public String getUserName()
    {
	return user.getUSERNAME();
    }
    
    
	public void setUser(SQLProfileRecord user) {
	if (user == null)
	{
	    clearUser();
	}
	else
	{
	    this.user = user;
	    this.g_UserId = user.getID();
	    this.g_Access = user.getAccess();
	}
    }
    
    public final void clearUser()
    {
	user = new SQLProfileRecord();
	this.g_UserId = -1;
	this.g_Access = -1;
    }
    
}

public class Bankapp8 {
    
    public LoggedInUser loggedInUser = new LoggedInUser();

    public final Scanner scanner = new Scanner(System.in);
  

    public static void launch(String[] args) {
	// TODO code application logic here
	
	Bankapp8 bapp = new Bankapp8();
	bapp.main(args);
    }
    

    public void main(String[] args) 
    {
	
	// System.out.println("RpcClient?");
	
	
//	System.out.println("Security disabled for demo");
//	System.out.println("Console logged in with admin access");
	
	    
	    
	    
	
	    mainloop: while (true)
	    {
		
		
		String msg; 
		
		if (this.loggedInUser.g_UserId >= 0)
		{
		    msg = "  00 quit\n"
			+ "  02 log out user "+this.loggedInUser.getUserName() + "\n"
			+ "  12 create a transaction, \n"
			+ "  21 view debit transactions from ID "+ this.loggedInUser.g_UserId+", \n"
			+ "  22 view credit transactions to ID "+ this.loggedInUser.g_UserId+", \n";
		    if (this.loggedInUser.g_Access >= 2)
			msg = msg  
			+ "  14 create new account\n"
			+ "  15 list accounts\n"
			+ "  16 display an account\n"
			+ "  17 verify a login/password\n"
			+ "  31 view transactions for specified ID, \n"
			+ "  90 create tables\n";
		}
		else
		{
		    msg = "  00 quit\n"
   		        + "  01 log in\n"
			+ "  14 create account\n"
			    + " 91 launch newframe gui";
		}
			    
		int which;
		which = scanlineInteger(msg);
		
		if (which < -1)
		{
		    
		    continue;
		}
		
		handleUserRequest(which);
		

		
		scanlineInteger("Type OK to continue");
	    }
    
    }
        public static void launchGUI2()
    {
	
	Thread GR; 
	GR = new Thread() 
	{
	    @Override
	    public void run() 
	    {
		try 
		{
		    GUI2.main(null);
		   
		    System.out.println("Launching newjframe gui2 app");
		    Thread.sleep(1);
		}
		catch(InterruptedException v)
		{
		}
	    }  
	};

	GR.start();
    }
    
    public void handleUserRequest(int which)  
    { 
	char ch;
	String ask;
	SQLDatabase db;
	// SQLProfileRecord profileRecord;
	// SQLTransactionRecord transactionRecord;
	
	handlechoice:
	switch (which) 
	{
	    case 91:
		launchGUI2();
		break;
	    case 0:
		this.loggedInUser.setUser(null);
		System.exit(0);
		
	    case 1:
	    {
		String email = this.promptForUsername("Login for which username or email?  ");
		String password = this.promptForUsername(email+"'s Password?  ");
		SQLProfileRecord  user = validateLogin(email, password);
		System.out.println("Login validated: " + (user != null));
		this.loggedInUser.setUser(user);
	    }
	    break;
		
	    case 2:
	    {
		this.loggedInUser.setUser(null);
		System.out.println("User logged out.");
		break;
	    }
		
	    case 12:
	    {
		SQLTransactionRecord transactionRecord = null;
		transactionRecord = this.askUserForInputForTransaction(scanner);
		if (transactionRecord != null)
		{
		    sendTransactionWithSQL(transactionRecord);
		}
		break;
	    }

	    case 21: 
	    {
		String id = ""+this.loggedInUser.g_UserId;
		db = new SQLDatabase();
		displayTransactionsFrom(db, id);
		break;
	    }
	
	    case 22: 
	    {
		// String id = this.promptForUsername("Get transactions from which user ID?  ");
		String id = ""+this.loggedInUser.g_UserId;
		db = new SQLDatabase();
		displayTransactionsTo(db, id);
		break;	
	    }

	    case 31: 
	    {
		db = new SQLDatabase();
		displayTransactionsFrom(db);
		break;
	    }
		
	    case 14:
	    {
		SQLProfileRecord profileRecord;		    
		profileRecord = askUserForInputForProfile(scanner);
		String result = Bankapp8.saveProfile(profileRecord);
		String s = "update ok?" + result;
		System.out.println(s);
		// if noone is logged in, set our login to
		// the newly created account.
		if (this.loggedInUser.isLoggedIn() == false)
		{
		    this.loggedInUser.setUser(profileRecord);
		}
		break;
	    }
		
	    case 15: 
		{
		    displayProfiles("");
		}
		break;
	    case 16: 
		{
		    String username = this.promptForUsername("Get profile for which email?  ");
		    displayProfiles(username);
		}
		break;
	
	    case 17:
		{
		    String email = this.promptForUsername("Verify account for email or username?  ");
		    String password = this.promptForUsername(email+"'s Password?  ");
		    SQLProfileRecord s = validateLogin(email, password);
		    System.out.println("Validated:" + (s!=null) );
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
    
    void displayTransactionsFrom(SQLDatabase db, String sFrom) 
    {
	    // RpcClient rpc;
	    // rpc = new RpcClient(); 
	    
	    // int howmany = scanlineInteger("How many records to display?");
	
	    ArrayList<Object> alist;
	    
	    // RPC call
	    int limit = 0;
	    
	    alist = db.getTransactions( "SENDER", sFrom, limit);
	    ArrayList<SQLTransactionRecord> transactionRecords;
	    
	    transactionRecords = SQLTransactionRecord.convertFromObjects(alist);
	    String s = SQLTransactionRecord.GetStringFromRecordList(transactionRecords);

	    System.out.println("SQLGetRecordsFromSender returned \n"+s);
	
	
    }    

    public ArrayList<String> displayTransactionsTo(SQLDatabase db, String sTo) 
    {
	ArrayList<String> ret = new ArrayList<>();
	ArrayList<Object> alist;
	    
	// RPC call

	int limit = 0;
	alist =	db.getTransactions(  "RECEIVER", sTo, limit);

	ArrayList<SQL.SQLTransactionRecord> transactionRecords;
	transactionRecords = SQL.SQLTransactionRecord.convertFromObjects(alist);
	String s = SQLTransactionRecord.GetStringFromRecordList(transactionRecords);
	ret.add(s);
	System.out.println("SQLGetRecordsFromSender returned \n"+s);
	return ret;
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
    
    public SQLProfileRecord validateLogin(String sID, String password) 
    {
	if (sID.equalsIgnoreCase(SQL.SQLNames.databaseUser))
	    if (password.equalsIgnoreCase(SQL.SQLNames.databasePassword))
	    {
		SQLProfileRecord s = new SQLProfileRecord();
		s.setAccess(2);
		return s;
	    }
    
	
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
		if (profile.getEMAIL().equalsIgnoreCase(sID) || 
			profile.getUSERNAME().equalsIgnoreCase(sID))
		{
		    if (profile.getPASSWORD().equalsIgnoreCase(password))
		    {
			return profile;
		    }
		}
	    }
	}
	return null;
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
		result += "replacing record "+RecordID+" for username "
			+record.getUSERNAME()+"\n";
		result += sqlDB.replaceProfile(RecordID,record);
	    }
	}
	else
	{
	    result += "inserting new record for username "
		    + record.getUSERNAME()+"\n";
	    result += sqlDB.insert(record);
	    
	}
	
	return result;
    }
    
    
    public SQLMessageRecord askUserForInputForMsg(Scanner scanner) {
	
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

public SQLTransactionRecord askUserForInputForTransaction(Scanner scanner) {

    	SQLTransactionRecord record;
	record = new SQLTransactionRecord();
	Double testval;
	String sendingOrReceiving = "S";
	boolean admin = (this.loggedInUser.g_Access >= 2);
	String sSender;
	String sReceiver;
	String id1; 
	String id2;
	String sProcessed = "PENDING";
	
	if (admin)
	{
	    System.out.println("Is this S (sending) or R (receiving)?");
	     sendingOrReceiving = scanner.nextLine();
	    
	    String p = "PENDING, FAILED, or PROCESSED";
	    System.out.println("Is it "+p);
	    sProcessed = scanner.nextLine();
	    if (p.contains(sProcessed) == false)
	    {
		System.out.println("Invalid input - must be "+p);
		return null;
	    }
	     
	    System.out.printf("%-20s :","From Bank User ID");
	    id1 = scanner.nextLine();
	    System.out.printf("%-20s :","To Bank User ID");
	    id2 = scanner.nextLine();
	}
	else
	{
	    id1 = ""+this.loggedInUser.g_UserId;
	    System.out.printf("%-20s :","To Bank User ID");
	    id2 = scanner.nextLine();
	}
	
	if (sendingOrReceiving.charAt(0)=='R')
	{
	    sReceiver = id1;
	    sSender = id2;
	}
	else
	{
	    sReceiver = id2;
	    sSender = id1;
	}
	
	// make sure we have a valid sender ID
	testval =Utils.parseDoubleSafely(sSender); 
	if ( testval.isNaN())
	{
	    String err = String.format("%s is not a numeric value.", sSender);
	    System.out.println(err);
	    return null;
	}
	else record.setSENDER(sSender);

	// make sure we have a valid recipient ID
	testval = Utils.parseDoubleSafely(sReceiver);
	if (testval.isNaN())
	{
	    String err = String.format("%s is not a numeric value.", sReceiver);
	    System.out.println(err);
	    return null;
	}
	
	else record.setRECEIVER(sReceiver);

	System.out.printf("%-20s :","Amount");
	String sAmount = scanner.nextLine();
	testval = Utils.parseDoubleSafely(sAmount); 
	if (testval.isNaN())
	{
	    String err = String.format("%s is not a numeric value.", sAmount);
	    System.out.println(err);
	    return null;
	}
	
	else 
	{
	    record.setAmount(sAmount);
	}
	
	record.setPROCESSED(sProcessed);
	System.out.printf("Transaction from %s to %s for $%.2f\n",
		record.getSENDER(),record.getRECEIVER(), record.getAmount());
	return record;
    }
    
    public SQLProfileRecord askUserForInputForProfile(Scanner scanner) 
    {
	int id = 0;
	String sEMAIL;
	String sPASSWORD;
	String sFIRSTNAME;
	String sLASTNAME;
	String sUSERNAME;
	String sADDRESS;
	String sBALANCE;
	String sACCESS;
	String sPHONENUMBER;
	
	System.out.printf("%20s:",SQLProfileRecord.EMAIL_F);
	sEMAIL = scanner.nextLine();

	System.out.printf("%20s:",SQLProfileRecord.PASSWORD_F);
	sPASSWORD = scanner.nextLine();

	System.out.printf("%20s:",SQLProfileRecord.FIRSTNAME_F);
	sFIRSTNAME = scanner.nextLine();
	
	System.out.printf("%20s:",SQLProfileRecord.LASTNAME_F);
	sLASTNAME = scanner.nextLine();

	System.out.printf("%20s:",SQLProfileRecord.USERNAME_F);
	sUSERNAME = sFIRSTNAME+"_"+sLASTNAME;
	System.out.println(sUSERNAME);
	
	System.out.printf("%20s:",SQLProfileRecord.PHONENUMBER_F);
	sPHONENUMBER = scanner.nextLine();

	System.out.printf("%20s:",SQLProfileRecord.ADDRESS_F);
	sADDRESS = scanner.nextLine();

	if (this.loggedInUser.g_Access > 1)
	{
	    System.out.printf("%20s:",SQLProfileRecord.ACCESS_F+" Employee? Y/N" );
	    sACCESS = scanner.nextLine();
	}
	else
	{
	    sACCESS = "N";
	}
	System.out.printf("%20s:",SQLProfileRecord.BALANCE_F);
	sBALANCE = scanner.nextLine();
	
	int iBALANCE;
	int iACCESS;

	iBALANCE = (int) (100.00 * Utils.parseDoubleSafely(sBALANCE));
	iACCESS = sACCESS.substring(0, 1).equalsIgnoreCase("Y") ? 2 : 1 ;
	
	SQLProfileRecord record;
	record = SQLProfileRecord.create_FromParameters
			(
			    id,
			    sUSERNAME,  
			    sPASSWORD, 
			    sFIRSTNAME, 
			    sLASTNAME,
			    sEMAIL,
			    sADDRESS,
			    iBALANCE,
			    iACCESS,
			    sPHONENUMBER
			); 		
	
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
	int which = -1;

	System.out.print(msg+": ");
	String s = scanner.nextLine(); 
	if (s.equalsIgnoreCase("OK")) return 1;
	else if (msg.contains(s))
	{
	    try
	    {

		which = Integer.parseInt(s);
		return which;
	    }
	    catch (Exception ex)
	    {
		which = -1;
		System.err.printf("Input (%s) not a valid integer\n",s);
	    }
	}
	if (which <= -1)
	    System.out.printf("%s is not a valid choice\n", s);
	return -1;
    }
    
    public static String sendTransactionWithSQL(SQLTransactionRecord record) {
	SQLDatabase db;
	db = new SQLDatabase();
	String result = db.insert(record);
	String s = "post ok?" + result;
	return result;
    }
    
    public static String sendMessageWithSQL(SQLMessageRecord messageRecord) {
	SQLDatabase db;
	db = new SQLDatabase();
	String result = db.insert(messageRecord);
	String s = "post ok?" + result;
	return result;
    }
/*
    public SQLTransactionRecord askUserForInputForTransaction(Scanner scanner) {
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
*/

    
    public String sendTransactionWithSQL_OLD(SQLTransactionRecord transactionRecord) {
	SQLDatabase db;
	db = new SQLDatabase();
	String result = db.insert(transactionRecord);
	String s = "post ok?" + result;
	return result;
    }

    private void displayTransactionsFrom(SQLDatabase db) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

