package com.bean;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Mail {

	String from="wstaw-sie@dadominik.pl";//"michyr@vp.pl";
	String host="smtp.googlemail.com";//"smtp.poczta.onet.pl";
	int PORT = 465;
	Properties properties;
	Session session;
	Authenticator authenticator;
	String USERNAME="wstaw-sie@dadominik.pl";
	String PASSWORD="weekPrayer5";
	
	Pair people;
	
	public Mail(Pair _people){

		setProperties();        
        session = Session.getDefaultInstance(properties, authenticator);		
		people=_people;
	}
	
	public void sendMail(){
	
	      try{
	         MimeMessage message = new MimeMessage(session);
	         message.setFrom(new InternetAddress(from));
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(people.one.address));

	         message.setSubject("Wstaw się!");
	         message.setText(createMessage(people.two.firstNameAddit, people.two.secondNameAddit));

	         Transport.send(message);
	         System.out.println("Message I sent successfully....");
	         
	         message = null;
	         message = new MimeMessage(session);
	         message.setFrom(new InternetAddress(from));
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(people.two.address));

	         message.setSubject("Wstaw się!");
	         message.setText(createMessage(people.one.firstNameAddit, people.one.secondNameAddit));

	         Transport.send(message);
	         System.out.println("Message II sent successfully....");
	         
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	}	
	
	private String createMessage(String first, String second){
		return "W tym tygodniu modlisz się za "+ first + " " + second + ".";
	}
	
	private Properties setProperties(){
		properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.port", PORT);
        properties.put("mail.smtp.ssl.enable", true);
        properties.put("mail.smtp.auth", true);
        
        authenticator = new Authenticator() {
                private PasswordAuthentication pa = new PasswordAuthentication(USERNAME, PASSWORD);
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return pa;
                }
            };
            
        return properties;
	}
}

