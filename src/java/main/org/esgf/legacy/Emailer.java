package org.esgf.legacy;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class Emailer {

	private static String EMAIL_HOST = "localhost";
	private static String EMAIL_FROM = "esgf-user@lists.llnl.gov";
	
	private String host;
	private String from;
	private String emailAddr;
	private String to;
	
	private String headerText;
	private String bodyText;
	
	private String attachment;
	
	private Multipart mp;
	
	public static void main(String [] args) {
		String to = "jfharney@gmail.com";
	    String from = "esgf-user@lists.llnl.gov";
	
	    String host = "localhost";
	
	    Properties properties = System.getProperties();
	
	    properties.setProperty("mail.smtp.host",host);
	
	    Session session = Session.getDefaultInstance(properties);
	
	    try {
	    	MimeMessage message = new MimeMessage(session);
	    	message.setFrom(new InternetAddress(from));
	
	    	message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	
	    	message.setSubject("This is the subject");
	    	message.setText("This is the message");
	
	    	Transport.send(message);
	
	    } catch(MessagingException mex) {
	    	mex.printStackTrace();
	    }
	
	}

	
	public Emailer(String emailAddr) {
		
		this.host = EMAIL_HOST;
		this.from = EMAIL_FROM;
		
	    this.to = emailAddr;
	    
	    this.emailAddr = emailAddr;
	    
	    this.setBodyText("Default Body Text");
	    this.setHeaderText("Default Header Text");
	    
	    this.mp = new MimeMultipart();
	    
	    
	    
	}
	
	public void sendEmail(String headerText,String bodyText) {
		this.sendEmail(headerText, bodyText, null);
	}
	
	public void sendEmail(String headerText,String bodyText,String [] fileNames) {
		
		Properties properties = System.getProperties();

	    properties.setProperty("mail.smtp.host",host);

	    Session session = Session.getDefaultInstance(properties);

	      System.out.println("FileNames: " + fileNames[0]);
	      
	      
	      
	    try {
	      MimeMessage message = new MimeMessage(session);

	      message.setFrom(new InternetAddress(from));

	      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

	      message.setSubject(headerText);

	      System.out.println("FileNames: " + fileNames[0]);
	      
	      if(fileNames == null) {
	    	  MimeBodyPart mbp1 = new MimeBodyPart();
	          mbp1.setText(bodyText);
		      //message.setText();
		      mp.addBodyPart(mbp1);
	      } else {
	    	  MimeBodyPart mbp1 = new MimeBodyPart();
	          mbp1.setText(bodyText);
		      //message.setText();
		      mp.addBodyPart(mbp1);
		      
		      for(int i=0;i<fileNames.length;i++) {
		    	  String fileName = fileNames[i];
		    	  MimeBodyPart mbp2 = new MimeBodyPart();
			      FileDataSource fds = new FileDataSource(fileName);

			      mbp2.setDataHandler(new DataHandler(fds));
			      mbp2.setFileName(fds.getName());
			      mp.addBodyPart(mbp2);
		      }
	      }
	      
	      message.setContent(mp);

	      Transport.send(message);


	    } catch(MessagingException mex) {
	      mex.printStackTrace();
	    }
		
	}
	
	
	


	public String getBodyText() {
		return bodyText;
	}
	
	
	
	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}
	
	
	
	public String getHeaderText() {
		return headerText;
	}
	
	
	
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}
	
	/**
	 * @return the attachment
	 */
	public String getAttachment() {
		return attachment;
	}
	
	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}


}