package SQL;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLProfileRecord implements SqlRecordInterface
{
    
    private static final String TABLENAME = "PROFILES";
    
    // member variables, and the information used to record
    // their values in the sql table
    //
    // _F    is the fieldname for that variable.
    // _TYPE is the data type used when
    //       creating that variable.
    //
    // WHEN YOU ADD OR REMOVE FIELDS, YOU MUST ALSO UPDATE
    // THE FOLLOWING METHODS!
    //
    // Sorry .. haven't figured out a way to modularize this more
    // than already done here.
    //
    // As soon as you add or remove a field, change these methods!
    //
    // getFieldNames 
    // getUpdateFieldList
    // create_FromParameters
    // create_fromResultSet(ResultSet results)
    // toString
    // GetSqlInsert
    // GetSqlReplace    

    private int	                ID = 0;
    private static final String ID_F =	    "ID";
    private static final String ID_TYPE = ID_F + " INTEGER NOT NULL";
	    
    
    private String username = "";
    public static final  String USERNAME_F =	   "USERNAME";
    private static final String USERNAME_TYPE = USERNAME_F+" VARCHAR(20) NOT NULL";
    
    private String password = "";
    public static final  String PASSWORD_F =    "PASSWORD"; 
    private static final String PASSWORD_TYPE = PASSWORD_F+" VARCHAR(20) NOT NULL";
    
    private String firstname = ""; 
    public static final  String FIRSTNAME_F =    "FIRSTNAME"; 
    private static final String FIRSTNAME_TYPE = FIRSTNAME_F+" VARCHAR(100) NOT NULL";
    
    private String lastname = "";
    public static final  String LASTNAME_F =	     "LASTNAME";
    private static final String LASTNAME_TYPE =   LASTNAME_F+" VARCHAR(100) NOT NULL";
    
    private String EMAIL = "";
    public static final  String EMAIL_F =          "EMAIL";
    private static final String EMAIL_TYPE =       EMAIL_F+" VARCHAR(100) NOT NULL";

    private String ADDRESS = "";
    public static final  String ADDRESS_F =    "ADDRESS";
    private static final String ADDRESS_TYPE = ADDRESS_F+" VARCHAR(60) NOT NULL";    
    
    private int BALANCE = 0;
    public static final  String BALANCE_F =    "BALANCE";
    private static final String BALANCE_TYPE = BALANCE_F+" INTEGER NOT NULL";    
    
    private int ACCESS = 0;
    public static final  String ACCESS_F =    "ACCESS";
    private static final String ACCESS_TYPE = ACCESS_F+" INTEGER NOT NULL";


    private String PHONENUMBER = "";
    public static final  String PHONENUMBER_F =    "PHONENUMBER";
    private static final String PHONENUMBER_TYPE = PHONENUMBER_F+" VARCHAR(20)";


    public static String getTableName() 
    {
	String s = SQLNames.sqlDB_SchemeName + "." + TABLENAME;
	return s;
    }
    
    @Override
    public String getFieldNames()
    {
	String fieldnames = 
	    USERNAME_F + ", " + 
	    PASSWORD_F +", " + 
	    FIRSTNAME_F + ", " + 
	    LASTNAME_F + ", "+
	    EMAIL_F+ ", "+
	    ADDRESS_F + ", " +	
	    BALANCE_F + ", " +
	    ACCESS_F + ", " +
	    PHONENUMBER_F;
	
	return fieldnames;
    }
    
    public String getUpdateFieldList() 
    {
	String updateFieldList =
		"UPDATE %s SET "
		+ USERNAME_F +	    " ='%s', "
		+ PASSWORD_F +	    " ='%s', "
		+ FIRSTNAME_F +	    " ='%s', "
	    	+ LASTNAME_F +	    " ='%s', "
		+ EMAIL_F +	    " ='%s', "
		+ ADDRESS_F +	    " ='%s', "
		+ BALANCE_F +	    " ='%s', "
		+ ACCESS +	    " =%d, "
		+ PHONENUMBER_F +   " ='%s' " // no comma on the last one
		+ "WHERE "+ ID_F +  "=%d";
	
	return updateFieldList;
    }
       
    static public SQLProfileRecord create_fromResultSet(ResultSet results) throws SQLException 
    {
	SQLProfileRecord record;

	int iId = results.getInt(ID_F);
	String sUSERNAME = results.getString(USERNAME_F);
	String sPASSWORD = results.getString(PASSWORD_F);
	String sFIRSTNAME = results.getString(FIRSTNAME_F);
	String sLASTNAME = results.getString(LASTNAME_F);
	String sEMAIL = results.getString(EMAIL_F);
	String sADDRESS = results.getString(ADDRESS_F);
	String sBALANCE = results.getString(BALANCE_F);
	String sACCESS = results.getString(PHONENUMBER_F);
	String sPHONENUMBER = results.getString(PHONENUMBER_F); 
	int iACCESS = Integer.parseInt(sACCESS);
	int iBALANCE = Integer.parseInt(sBALANCE);
	
	record = create_FromParameters(
			    iId,
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
    
    public static ArrayList<SQLProfileRecord> convertFromObjects(ArrayList<Object> alist) 
    {
	ArrayList<SQLProfileRecord> blist = new ArrayList();
	
	for (Object a: alist)
	{
	    blist.add((SQLProfileRecord) a);
	}
	return blist;
    }

     public static String GetStringFromRecordList(ArrayList<SQLProfileRecord> records) {
	String s = "";
	for (SQLProfileRecord rec: records)
	{
	    s+= rec+"\n";
	}
	return s;
    }
   
    
    
    public SQLProfileRecord()
    {
    }
    
    public static SQLProfileRecord create_FromParameters(
					    int	    iId, 
					    String  sUSERNAME, 
					    String  sPASSWORD, 
					    String  sFIRSTNAME, 
					    String  sLASTNAME, 
					    String  sEMAIL,
					    String  sADDRESS,
					    int	    iBALANCE,
					    int	    iACCESS,
					    String  sPHONENUMBER 
					   ) 
    {
	SQLProfileRecord me = new SQLProfileRecord();
	me.ID = iId;
	me.username = sUSERNAME;
	me.password  = sPASSWORD;
	me.firstname = sFIRSTNAME;
	me.lastname = sLASTNAME;
	me.EMAIL = sEMAIL;
	me.ADDRESS = sADDRESS;
	me.BALANCE = iBALANCE;
	me.ACCESS = iACCESS;
	me.PHONENUMBER = sPHONENUMBER;
	
	return me;
    }
    
    @Override
    public String toString() {
	SQLProfileRecord record = this;
	return toString(record);

    }

    public static String toString(SQLProfileRecord me) {
	String s = String.format(ID_F		+ ":%-10d "
		+ USERNAME_F    + ":%-20s "
		+ PASSWORD_F    + ":%-20s "
		+ FIRSTNAME_F   + ":%-20s "
		+ LASTNAME_F    + ":%-20s "
		+ EMAIL_F       + ":%-12s "
		+ ADDRESS_F     + ":%-30s "
		+ BALANCE_F	+ ":%-20.2f "
		+ ACCESS_F	+ ":%-2d "
		+ PHONENUMBER_F + ":%-20s "
		,
		me.ID,
		me.username,
		me.password,
		me.firstname,
		me.lastname,
		me.EMAIL,
		me.ADDRESS,
		me.BALANCE / 100.00,
		me.ACCESS,
    		me.PHONENUMBER
		);
	return s;

    }
    
    
    static String string = " '%s' ";
    static String integer = " %d ";
    static String comma = ",";

    @Override
    public String GetSqlInsert()
    {
	// note that ID is generated automatically by the
	// SQL database.
	
	String s = String.format("INSERT INTO %s (%s) VALUES "+
		"("
		+ string  + comma	// username
		+ string  + comma	// password
		+ string  + comma	// firstname
		+ string  + comma	// lastname
		+ string  + comma	// email
		+ string  + comma	// address
		+ integer + comma	// balance
		+ integer  +comma	// access
		+ string		// phonenumber NO COMMA ON LAST ONE 
		+ ")",
		    getTableName(), 
		    getFieldNames(),
		    this.username,
		    this.password,
		    this.firstname,
		    this.lastname,
		    this.EMAIL,
		    this.ADDRESS,
		    this.BALANCE,
		    this.ACCESS,
		    this.PHONENUMBER
		);
	// System.err.println("SQL:"+s);
	return s;
    }

    public int getID() {
	return ID;
    }

    
    // this needs to be improved

    
    public static String getCreateCommand() 
    {
	String newlinetab = "\n\t";
	
	
	String s = "CREATE TABLE "+getTableName() + "(" + newlinetab + 
		    ID_TYPE + newlinetab +
		    "PRIMARY KEY GENERATED ALWAYS AS IDENTITY " + newlinetab +
		    "(START WITH 1, INCREMENT BY 1),"  + newlinetab +	
		USERNAME_TYPE	    + comma + newlinetab +
		PASSWORD_TYPE	    + comma + newlinetab +
		FIRSTNAME_TYPE	    + comma + newlinetab +
		LASTNAME_TYPE	    + comma + newlinetab +
		EMAIL_TYPE	    + comma + newlinetab +
		ADDRESS_TYPE	    + comma + newlinetab +
		BALANCE_TYPE	    + comma + newlinetab +
		ACCESS_TYPE	    + comma + newlinetab + 
		PHONENUMBER_TYPE    + newlinetab + // no comma on last field
	    ")";
	return s;
    }

    public String getUSERNAME() {
	return username;
    }

    public void setUSERNAME(String USERNAME) {
	this.username = USERNAME;
    }

    public String getPASSWORD() {
	return password;
    }

    public void setPASSWORD(String PASSWORD) {
	this.password = PASSWORD;
    }

    public String getFirstname() {
	return firstname;
    }

    public void setFirstname(String firstname) {
	this.firstname = firstname;
    }

    public String getLASTNAME() {
	return lastname;
    }

    public void setLASTNAME(String LASTNAME) {
	this.lastname = LASTNAME;
    }

    public String getEMAIL() {
	return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
	this.EMAIL = EMAIL;
    }
    
    public String getADDRESS() {
	return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
	this.ADDRESS = ADDRESS;
    }
    
    public String getBALANCE() {
	String s = String.format("%.2f",BALANCE / 100.0);
	return PHONENUMBER;
    }

    public void setBALANCE(String BALANCE) {
	this.BALANCE = (int) ( Double.parseDouble(BALANCE) * 100.0);
    }

    public String getPHONENUMBER() {
	return PHONENUMBER;
    }

    public void setPHONENUMBER(String PHONENUMBER) {
	this.PHONENUMBER = PHONENUMBER;
    }



    String GetSqlReplace(int RecordID) 
    {
	
	String s = String.format(getUpdateFieldList(),
		    getTableName(), 
		    this.username,
		    this.password,
		    this.firstname,
		    this.lastname,
		    this.EMAIL,
		    this.BALANCE,
		    this.ACCESS,
		    this.PHONENUMBER,
		    RecordID
		);
	// System.err.println("UPDATE Set SQL:\n"+s);
	return s;
    }
    @Override
    public String tableName() {
	return TABLENAME;
    }

    public int getAccess() {
	return ACCESS;
    }

    public void setAccess(int access) {
	this.ACCESS = access;
    }
}
