package com.murphy.taskmgmt.util;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.integration.util.ApplicationConstant;

public class MailAlertUtil {

	private static final Logger logger = LoggerFactory.getLogger(MailAlertUtil.class);
	
	public String sendMailWithCC(String receipentId, String cc, String subjectName, String message, String userName) {
		Properties props = new Properties();
		props.put(MurphyConstant.SMTP_AUTH, "true");
		props.put(MurphyConstant.SMTP_TTLS, "true");
		props.put(MurphyConstant.MAIL_HOST, "outlook.office365.com");
		props.put(MurphyConstant.SMTP_PORT, "587");
		props.put(MurphyConstant.TRANSPORT_PROTOCOL, "smtp");
		props.put(MurphyConstant.BOUNCER_PORT, MurphyConstant.BOUNCER_ID);
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(MurphyConstant.SENDER_MAILID, MurphyConstant.SENDER_PASSWORD);
			}
		});
		MimeMessage mimeMesg = new MimeMessage(session);
		try {
			mimeMesg.setFrom(new InternetAddress(MurphyConstant.SENDER_MAILID));
			getMailBodyWithCC(receipentId, cc, mimeMesg, subjectName, message, userName);
			Transport.send(mimeMesg);

		} catch (Exception e) {
			logger.error("[Murphy][MailAlertUtil][sendMailWithCC][Exception]" + e.getMessage());
			return MurphyConstant.FAILURE;
		}
		
		return MurphyConstant.SUCCESS;
	}

	private void getMailBodyWithCC(String receipentId, String cc, MimeMessage message, String subjectName,
			String messagebody, String userName) {
		Object msg = null;
		try {
			InternetAddress[] to = InternetAddress.parse(receipentId);
			message.setRecipients(Message.RecipientType.TO, to);
			if (!ServicesUtil.isEmpty(cc)) {
				InternetAddress[] cC = InternetAddress.parse(cc);
				message.setRecipients(Message.RecipientType.CC, cC);
			}
			Multipart multipart = new MimeMultipart();
			message.setSubject(subjectName);
			BodyPart messageBodyPart = new MimeBodyPart();

			msg = "<html> <body>Hi  " + userName + ",</br></br>" + messagebody
					+ "</br><font color=grey>This is a system generated e-mail. Do-not reply.</font></br>";

			messageBodyPart.setContent(msg, MurphyConstant.MAIL_UTF8_CONTENT_TYPE);
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			message.saveChanges();
		} catch (Exception ex) {
			logger.error("[Murphy][MailAlertUtil][getMailBodyWithCC][Exception]" + ex.getMessage());
		}

	}

	public String sendMailAlert(String receipentId, String subjectName, String message, String userName,String moduleLink,File file) {

		Properties props = new Properties();
		props.put(MurphyConstant.SMTP_AUTH, "true");
		props.put(MurphyConstant.SMTP_TTLS, "true");
		props.put(MurphyConstant.MAIL_HOST, "outlook.office365.com");
		props.put(MurphyConstant.SMTP_PORT, "587");
		props.put(MurphyConstant.TRANSPORT_PROTOCOL,"smtp");
		props.put(MurphyConstant.BOUNCER_PORT,MurphyConstant.BOUNCER_ID);
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(MurphyConstant.SENDER_MAILID, MurphyConstant.SENDER_PASSWORD);
			}
		});
		MimeMessage mimeMesg = new MimeMessage(session);
		try {
			mimeMesg.setFrom(new InternetAddress(MurphyConstant.SENDER_MAILID));
            if(!ServicesUtil.isEmpty(file)){
            	createMailBodyForEnergyIsolation(file,receipentId, mimeMesg, subjectName, message, userName);
            }else{
			getMailBody(receipentId, mimeMesg, subjectName, message, userName,moduleLink);
            }
			Transport.send(mimeMesg);

		} catch (Exception e) {
			logger.error("[sendMailtoClient]:Exception while sending mail " + e.getMessage());
            return MurphyConstant.FAILURE;
		}
		return MurphyConstant.SUCCESS;
	}

	private void createMailBodyForEnergyIsolation(File file, String receipentId, MimeMessage mimeMesg,
			String subjectName, String message, String fileName) {
 		try{
 			InternetAddress[] parse = InternetAddress.parse(receipentId);
 			mimeMesg.setRecipients(Message.RecipientType.TO, parse);
 			mimeMesg.setSubject(subjectName);
 			BodyPart messageBodyPart = new MimeBodyPart();
 			Multipart multipart = new MimeMultipart();
 			Object msg = "<html> <body>Hello,</br></br>" + message
					+"</br></br><font color=grey>This is a system generated e-mail. Do-not reply.</font></br>";
 			messageBodyPart.setContent(msg,MurphyConstant.MAIL_UTF8_CONTENT_TYPE);
 			multipart.addBodyPart(messageBodyPart);
 			messageBodyPart = new MimeBodyPart();
 			DataSource source = new FileDataSource(file.getAbsolutePath());
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);
			mimeMesg.setContent(multipart);
			mimeMesg.saveChanges();
 			 
 		}
 		catch(Exception e){
 			logger.error("[Murphy][MailAlertUtil][createMailBodyForEnergyIsolation][Exception]"+e.getMessage());
 			e.printStackTrace();
 		}
	}

	private void getMailBody(String receipentId, MimeMessage message, String subjectName, String messagebody,
			String userName,String moduleLink) {
		Object msg=null;
		try {
			InternetAddress[] parse = InternetAddress.parse(receipentId);
			message.setRecipients(Message.RecipientType.TO, parse);
			Multipart multipart = new MimeMultipart();
			message.setSubject(subjectName);
			BodyPart messageBodyPart = new MimeBodyPart();
				
			if(!ServicesUtil.isEmpty(moduleLink))
				
				 msg = "<html> <body>Hello " + userName + ",</br></br>" + messagebody + "<a href="
						+ moduleLink
						+ "> click here"
						+ "</a> to see the details.</br>"
						+ "</br><font color=grey>This is a system generated e-mail. Do-not reply.</font></br>";
				else
					
//					 msg = "<html> <body>Hello  " + userName + ",</br></br>" + messagebody
//							+ ".</br></br>Thanks,</br>Murphy-IOP team.</br></br></body></html>";
					msg = "<html> <body>Hello  " + userName + ",</br></br>" + messagebody
					+"</br><font color=grey>This is a system generated e-mail. Do-not reply.</font></br>";
				

			messageBodyPart.setContent(msg, MurphyConstant.MAIL_UTF8_CONTENT_TYPE);
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			message.saveChanges();
		} catch (Exception ex) {
			logger.error("Exception While Sending Mail Alert" + ex.getMessage());
		}
	}
}