package org.esgf.srm.email;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Email {
	
	private String headerText;
	private String bodyText;
	private String to;
	private String from;
	private String [] fileNames;
	private String wgetContent;
	private String scriptFileName;
	
	public static void main(String [] args) {
		
		String [] fileNames = {"http://yahoo.com"};
		
		
		Email e = new Email();
		
		e.sendEmail();
	}

	
	public Email() {
		
		this.from = "";
		this.to = EmailUtils.RECEIVER_USERNAME;
		this.headerText = EmailUtils.DEFAULT_HEADER_TEXT;
		this.bodyText = EmailUtils.DEFAULT_BODY_TEXT;
		this.setFileNames(EmailUtils.DEFAULT_FILE_NAMES);

		this.scriptFileName = EmailUtils.DEFAULT_SCRIPT_FILE_NAME;
		this.writeWgetContent(fileNames);
	}
	
	public Email(String [] fileNames) {
		this.from = "";
		this.to = EmailUtils.RECEIVER_USERNAME;
		this.headerText = EmailUtils.DEFAULT_HEADER_TEXT;
		this.bodyText = EmailUtils.DEFAULT_BODY_TEXT;
		this.setFileNames(fileNames);

		this.scriptFileName = EmailUtils.DEFAULT_SCRIPT_FILE_NAME;
		writeWgetContent(fileNames);
	}

	public Email(String headerText,String bodyText,String to,String from,String [] fileNames) {
		this.setHeaderText(headerText);
		this.setBodyText(bodyText);
		this.setTo(to);
		this.setFrom(from);
		this.setFileNames(EmailUtils.DEFAULT_FILE_NAMES);
		
		//write wget content here
		this.scriptFileName = EmailUtils.DEFAULT_SCRIPT_FILE_NAME;
		writeWgetContent(fileNames);
		this.fileNames = fileNames;
	}
	
	public void writeWgetContent(String [] fileNames) {
		
		this.wgetContent = "";
		this.wgetContent = "wget ";
		for(int i=0;i<fileNames.length;i++) {
			this.wgetContent += fileNames[i] + " ";
		}
		System.out.println("Wget content " + this.wgetContent);
	}

	public void sendEmail() {
		Properties props = new Properties();
		props.put("mail.smtp.host", EmailUtils.MAIL_SMTP_HOST);
		props.put("mail.smtp.socketFactory.port", EmailUtils.MAIL_SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", EmailUtils.MAIL_SMTP_SOCKETFACTORY_CLASS);
		props.put("mail.smtp.auth", EmailUtils.MAIL_SMTP_AUTH);
		props.put("mail.smtp.port", EmailUtils.MAIL_SMTP_PORT);
		
		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(EmailUtils.SENDER_USERNAME,EmailUtils.SENDER_PASSWORD);
					}
				});
	 
		try {
			 
			Message message = new MimeMessage(session);
			//message.setFrom(new InternetAddress("from@no-spam.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(this.to));
			message.setSubject(this.headerText);
			//message.setText(this.bodyText);
 
			
			Multipart mp = new MimeMultipart();
			
			//this.scriptFileName = this.fileNames[0];
	    	
			
			if(this.fileNames == null) {
				MimeBodyPart mbp1 = new MimeBodyPart();
				mbp1.setText(this.bodyText);
			      //message.setText();
				mp.addBodyPart(mbp1);
		    } else {
		    	MimeBodyPart mbp1 = new MimeBodyPart();
		    	mbp1.setText(this.bodyText);
			      
		    	//message.setText();
		    	mp.addBodyPart(mbp1);
			      
		    	MimeBodyPart mbp2 = new MimeBodyPart();
		    	FileDataSource fds = new FileDataSource(this.scriptFileName);

		    	writeFileAttachment();
		    	
		    	mbp2.setDataHandler(new DataHandler(fds));
		    	mbp2.setFileName(fds.getName());
		    	mp.addBodyPart(mbp2);
			      
		    }
			
			message.setContent(mp);

	    	
			Transport.send(message);

			deleteFileAttachment();
			
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
 
	}
	
	
	/**
	 * @return the headerText
	 */
	public String getHeaderText() {
		return headerText;
	}

	/**
	 * @param headerText the headerText to set
	 */
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}

	/**
	 * @return the bodyText
	 */
	public String getBodyText() {
		return bodyText;
	}

	/**
	 * @param bodyText the bodyText to set
	 */
	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the fileNames
	 */
	public String [] getFileNames() {
		return fileNames;
	}

	/**
	 * @param fileNames the fileNames to set
	 */
	public void setFileNames(String [] fileNames) {
		this.fileNames = fileNames;
	}
	
	public void deleteFileAttachment() {
		
		FileOutputStream fop = null;
		File file;
		String content = "This is the text content";
 
		try {
			 
			file = new File(this.scriptFileName);
			fop = new FileOutputStream(file);
 
			
			if(file.delete()){
				System.out.println(file.getName() + " is deleted!");
			}else{
				System.out.println("Delete operation is failed.");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeFileAttachment() {
		FileOutputStream fop = null;
		File file;
		String content = this.wgetContent;
 
		try {
 
			file = new File(this.scriptFileName);
			
			fop = new FileOutputStream(file);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	
    	
	}

	/**
	 * @return the wgetContent
	 */
	public String getWgetContent() {
		return wgetContent;
	}

	/**
	 * @param wgetContent the wgetContent to set
	 */
	public void setWgetContent(String wgetContent) {
		this.wgetContent = wgetContent;
	}
	
}
