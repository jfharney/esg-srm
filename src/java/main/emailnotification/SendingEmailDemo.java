package emailnotification;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.io.*;
 

public class SendingEmailDemo {
	public static void main(String[] args) {
		String from = "esonu@uga.edu";
        String to = "ekhlasssonu@yahoo.com";
        String subject = "Hi There...";
        String body = "How are you?";
        String host = "smtp.gmail.com";
        String port = "25";
        
		Properties prop = new Properties();
		try {
            prop.load(new FileInputStream("../mail.properties"));
            host = prop.getProperty("mailHost");
            port = prop.getProperty("mailPort");
            from = prop.getProperty("mailFrom");
            to = prop.getProperty("mailTo");
            subject = prop.getProperty("mailSubject");
            body = prop.getProperty("mailBody");
	    } catch (Exception e) {
	    	System.out.println("SendingEmailDemo: Something Went wrong while reading the property file");
	    }
		
//        String from = "ekhlas.sonu@gmail.com";
		
 
        //
        // A properties to store mail server smtp information such 
        // as the host name and the port number. With this properties 
        // we create a Session object from which we'll create the 
        // Message object.
        //
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
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
 
            //
            // Send the message to the recipient.
            //
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
