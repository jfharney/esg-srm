package org.esgf.srm;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Emailer1 {

	private static String EMAIL_HOST = "smtp.gmail.com";
	private static String EMAIL_FROM = "jfharney@gmail.com";
	private static String EMAIL_PASSWORD = "1Ring2RuleThemAll";
	
	private String host;
	private String from;
	private String pass;
	private String emailAddr;
	private String [] to;
	
	
	public Emailer1(String emailAddr) {
		
		this.host = EMAIL_HOST;
		this.from = EMAIL_FROM;
		this.pass = EMAIL_PASSWORD;
		
		

	    this.to = new String[1];
	    this.to[0] = emailAddr;
	    
	    
	}
	
	public void sendEmail(String headerText,String bodyText) {
		
		Properties props = System.getProperties();
	    props.put("mail.smtp.starttls.enable", "true"); // added this line
	    props.put("mail.smtp.host", "localhost");
	    props.put("mail.smtp.user", from);
	    props.put("mail.smtp.password", pass);
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.auth", "true");
	    
		Session session = Session.getDefaultInstance(props, null);
	    MimeMessage message = new MimeMessage(session);
	    try {
			message.setFrom(new InternetAddress(from));
			
			InternetAddress[] toAddress = new InternetAddress[to.length];

		    // To get the array of addresses
		    for( int i=0; i < to.length; i++ ) { // changed from a while loop
		        toAddress[i] = new InternetAddress(to[i]);
		    }

		    for( int i=0; i < toAddress.length; i++) { // changed from a while loop
		        message.addRecipient(Message.RecipientType.TO, toAddress[i]);
		    }
		    
		    message.setSubject(headerText);
		    message.setText(bodyText);
		    Transport transport = session.getTransport("smtp");
		    transport.connect(host, from, pass);
		    transport.sendMessage(message, message.getAllRecipients());
		    transport.close();
		    
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    
	    
	}
	
	public static void main(String [] args) throws AddressException, MessagingException {
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
 
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("jfharney","1Ring2RuleThemAll");
				}
			});
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from@no-spam.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("jfharney@gmail.com"));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler," +
					"\n\n No spam to my email, please!");
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
 
		
		
		
		
		
		
		
		/* USE THIS
		String emailAddr = "jfharney@gmail.com";
		
		Emailer1 emailer = new Emailer1(emailAddr);
		
		String headerText = "HeaderText";
		String bodyText = "BodyText";
		
		emailer.sendEmail(headerText, bodyText);
		*/
		
		
		
		
		
		/*
		String host = EMAIL_HOST;
	    String from = EMAIL_FROM;
	    String pass = EMAIL_PASSWORD;
	    Properties props = System.getProperties();
	    props.put("mail.smtp.starttls.enable", "true"); // added this line
	    props.put("mail.smtp.host", host);
	    props.put("mail.smtp.user", from);
	    props.put("mail.smtp.password", pass);
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.auth", "true");

	    String[] to = {"jfharney@gmail.com"}; // added this line

	    Session session = Session.getDefaultInstance(props, null);
	    MimeMessage message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(from));

	    InternetAddress[] toAddress = new InternetAddress[to.length];

	    // To get the array of addresses
	    for( int i=0; i < to.length; i++ ) { // changed from a while loop
	        toAddress[i] = new InternetAddress(to[i]);
	    }

	    for( int i=0; i < toAddress.length; i++) { // changed from a while loop
	        message.addRecipient(Message.RecipientType.TO, toAddress[i]);
	    }
	    
	    String headerText1 = "Your File Request has been submitted!";
	    String headerText2 = "Your File Request has been completed!";
	    
	    
	    
	    message.setSubject("sending in a group");
	    message.setText("Welcome to JavaMail");
	    Transport transport = session.getTransport("smtp");
	    transport.connect(host, from, pass);
	    transport.sendMessage(message, message.getAllRecipients());
	    transport.close();
	    */
		
	}
	
	
}
