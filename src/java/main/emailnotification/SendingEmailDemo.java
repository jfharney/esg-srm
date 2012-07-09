package emailnotification;

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
 

public class SendingEmailDemo {
	public static void main(String[] args) {
		String from = "sonues@ornl.gov";
        String to = "ekhlas.sonu@gmail.com";
        String subject = "Hi There...";
        String body = "How are you?";
        String host = "smtp.mail.ornl.gov";
        String port = "443";
        String password = "";
        
		Properties prop = new Properties();
		try {
//			System.out.println("In try block");
            prop.load(new FileInputStream("./src/java/main/mail.properties"));
//            System.out.println("Reading");
            host = prop.getProperty("mailHost");
            port = prop.getProperty("mailPort");
            from = prop.getProperty("mailFrom");
            to = prop.getProperty("mailTo");
            subject = prop.getProperty("mailSubject");
            body = prop.getProperty("mailBody");
            password = prop.getProperty("pass");
            
//            System.out.println("Host: "+ host +"\nPort: "+ port + "\nFrom: " + from + "\nTo: " + to + "\nSubject: " + subject + "\nBody: " + body);
	    } catch (Exception e) {
	    	System.out.println("SendingEmailDemo: Something went wrong while reading the property file");
	    }
		
		
//        String from = "ekhlas.sonu@gmail.com";
		
 
        //
        // A properties to store mail server smtp information such 
        // as the host name and the port number. With this properties 
        // we create a Session object from which we'll create the 
        // Message object.
        //
        Properties properties = new Properties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.user", "e1g");
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
            System.out.println("Connecting...");
            transport.connect(host, "e1g", password);
            System.out.println("Connection established... Sending email...");
            transport.sendMessage(message, message.getAllRecipients());
            System.out.println("Closing connection...");
            transport.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
