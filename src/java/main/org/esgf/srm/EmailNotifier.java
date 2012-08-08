package org.esgf.srm;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.activation.*;

import java.util.Date;
import java.util.Properties;
import java.io.*;

/**
 * @author      Ekhlas Sonu <esonu@uga.edu>
 * @version     1.0                                    
 * @since       2012-08-07          
 */

public class EmailNotifier {
	private String from;
	private String user;
	private String to;
	private String subject;
	private String body;
	private String host;
	private String port;
	private String password;
	
	/**
	 * Constructor    
	 *
	 * Constructor of the class. Initialize mailing options.
	 * 
	 * @param args Array containing options 
	 * -to <receiver email id>
	 * -from <sender email id>
	 * -user <sender user id> for authentication
	 * -subject <subject text>
	 * -body <body text>
	 * -host <email smtp host>
	 * -port <smtp port> usually 25
	 * -password <user password> for authentication
	 */
	
	public EmailNotifier(String[] args){
		from = "";
		user = "";
		to = "";
		subject = "Request Status";
		body = "Your request has been submitted";
		host = "smtp.ornl.gov";
		port = "25";
		password = "";
		parseArguments(args);
	}
	
	
	/**
	 * Parse arguments to constructor  
	 *
	 * @param args Array containing options 
	 * -to <receiver email id>
	 * -from <sender email id>
	 * -user <sender user id> for authentication
	 * -subject <subject text>
	 * -body <body text>
	 * -host <email smtp host>
	 * -port <smtp port> usually 25
	 * -password <user password> for authentication
	 */
	public void parseArguments(String[] args){
		for(int i=0; i < args.length; i++){
			if(args[i].equalsIgnoreCase("-to") && i+1 < args.length){
				to=args[++i];
			}
			if(args[i].equalsIgnoreCase("-from") && i+1 < args.length){
				from=args[++i];
			}
			if(args[i].equalsIgnoreCase("-user") && i+1 < args.length){
				user=args[++i];
			}
			if(args[i].equalsIgnoreCase("-subject") && i+1 < args.length){
				subject=args[++i];
			}
			if(args[i].equalsIgnoreCase("-body") && i+1 < args.length){
				body=args[++i];
			}
			if(args[i].equalsIgnoreCase("-host") && i+1 < args.length){
				host=args[++i];
			}
			if(args[i].equalsIgnoreCase("-port") && i+1 < args.length){
				port=args[++i];
			}
			if(args[i].equalsIgnoreCase("-password") && i+1 < args.length){
				password=args[++i];
			}
		}
	}
	
	
	public String getFrom() {
		return from;
	}


	public void setFrom(String from) {
		this.from = from;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getTo() {
		return to;
	}


	public void setTo(String to) {
		this.to = to;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}


	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public String getPort() {
		return port;
	}


	public void setPort(String port) {
		this.port = port;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * Method to send email
	 * 
	 * @throws IOException
	 */
	
	public void sendMail() throws IOException {
 
        //
        // A properties to store mail server smtp information such 
        // as the host name and the port number. With this properties 
        // we create a Session object from which we'll create the 
        // Message object.
        //
        Properties properties = new Properties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.user", user);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.password", password);
        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.debug", "true");
        properties.setProperty("mail.smtp.quitwait", "false");
        
        Session session = Session.getDefaultInstance(properties, null);
 
        try {
            //
            // Message is a mail message to be send through the 
            // Transport object. In the Message object we set the 
            // sender address and the recipient address. Both of 
            // this address is a type of InternetAddress. For the 
            // recipient address we can also set the type of 
            // recipient, the value can be TO, CC or BCC. In the next
            // two lines we set the email subject and the content text.
            //
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body);
            message.setSentDate(new Date());
 
            //
            // Send the message to the recipient.
            //
//          Transport.send(message);
            
            
            
            message.saveChanges();
            Transport transport = session.getTransport("smtp");
            System.out.println("Connecting to mail server...");
            transport.connect(host, user, password);
            System.out.println("Connection established... Sending email...");
            transport.sendMessage(message, message.getAllRecipients());
            System.out.println("Closing connection...");
            transport.close();
            
            
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
	
	
}



