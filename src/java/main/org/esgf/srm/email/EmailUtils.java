package org.esgf.srm.email;

public class EmailUtils {

	
	//smtp params
	public static String MAIL_SMTP_HOST = "smtp.gmail.com";
	public static String MAIL_SMTP_SOCKETFACTORY_PORT = "465";
	public static String MAIL_SMTP_SOCKETFACTORY_CLASS = "javax.net.ssl.SSLSocketFactory";
	public static String MAIL_SMTP_AUTH = "true";
	public static String MAIL_SMTP_PORT = "465";

	//account params (will take out later)
	public static String SENDER_USERNAME = "jfharney";
	public static String SENDER_PASSWORD = "1Ring2RuleThemAll";
	
	//receiver
	public static String RECEIVER_USERNAME = "jfharney@gmail.com";
	
	//header and body text
	public static String DEFAULT_HEADER_TEXT = "Your data has been successfully staged!";
	public static String DEFAULT_BODY_TEXT = "Dear ESGF User, " +
			"\nThe data that you have ordered is now available on disk.  Please download the attached wget script to extract the files.";
	
	//default files names to be extracted
	public static String [] DEFAULT_FILE_NAMES = {"http://google.com","http://yahoo.com"};
	
	//default wget script name
	public static final String DEFAULT_SCRIPT_FILE_NAME = "wget.sh";
	
}
