package SQL;


import SQL.SqlClient;
import SQL.SQLMessageRecord;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utils 
{
    
    public static String getSendTimeString(long milliseconds)
    {
	Date d = convertLocalTimestamp(milliseconds);
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(d);
	int hours = calendar.get(Calendar.HOUR_OF_DAY);
	int minutes = calendar.get(Calendar.MINUTE);
	int seconds = calendar.get(Calendar.SECOND);	
	String time = String.format("%02d.%02d.%02d", hours, minutes, seconds);	
	return time;
    }
    

public static Date convertLocalTimestamp(long millis)
{
    TimeZone tz = TimeZone.getDefault();
    Calendar c = Calendar.getInstance(tz);
    long localMillis = millis;
    int offset, time;

    c.set(1970, Calendar.JANUARY, 1, 0, 0, 0);

    // Add milliseconds
    while (localMillis > Integer.MAX_VALUE)
    {
        c.add(Calendar.MILLISECOND, Integer.MAX_VALUE);
        localMillis -= Integer.MAX_VALUE;
    }
    c.add(Calendar.MILLISECOND, (int)localMillis);

    // Stupidly, the Calendar will give us the wrong result if we use getTime() directly.
    // Instead, we calculate the offset and do the math ourselves.

    time = c.get(Calendar.MILLISECOND);
    time += c.get(Calendar.SECOND) * 1000;
    time += c.get(Calendar.MINUTE) * 60 * 1000;
    time += c.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000;
    offset = tz.getOffset(c.get(Calendar.ERA), c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_WEEK), time);

    return new Date(millis - offset);
}    
    
    public static SQLMessageRecord makeTestRecord(String from, String to, String subject, String msg)
    {
	SQLMessageRecord record;
	record = new SQLMessageRecord();
	record.setSENDER(from);
	record.setRECEIVER(to);
	record.setSUBJECT(subject);
	record.setMESSAGE(msg);
	return record;
    }
    public static ArrayList<String> getStringListFromObjectList(ArrayList<Object> sendResult) {
	ArrayList<String> slist = new ArrayList();
	for (Object o : sendResult)
	{
	    slist.add((String) o);
	}
	return slist;
    }

    public static String getStringFromStringList(ArrayList<String> slist) {
	String sResult = "";
	for (String s: slist)
	{
	    sResult += s+"\n";
	}
	return sResult;
    }    

    public static String checkSQLConnection()
    {
	SqlClient chatSQL = new SqlClient();
	String sErr = chatSQL.createConnection();
	
	if (sErr.length() == 0) 
	{
	    SQLMessageRecord record;
	    record = Utils.makeTestRecord("SERVERTEST","SERVERTESTRESULT","SERVER LAUNCH","SERVER LAUNCH OK");
	    sErr = chatSQL.insertMessage(record);
	}
	return sErr;
    }
}
