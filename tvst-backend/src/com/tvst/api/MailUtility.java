/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tvst.api;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author pedja
 */
public class MailUtility
{

    public static void sendVerificationEmail(User user) throws MessagingException
    {
        String subject = "TV Show Tracker - Account Verification";
        String message = "<html><body>Hello <b>" + user.getEmail() + "</b>,<br>"
                + "Thank you for your registration.<br> To verify your account please click this link:<br>"
                + user.getRegistration_key() + "</body></html>";
        Properties props = new Properties();
        props.setProperty("mail.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");

        Authenticator auth = new SMTPAuthenticator("login", "password");//TODO change

        Session session = Session.getInstance(props, auth);

        MimeMessage msg = new MimeMessage(session);
        msg.setText(message);
        msg.setSubject(subject);
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
        msg.addHeader("Reply-To", "test@server.com");
        msg.addHeader("MIME-Version", "1.0");
        msg.addHeader("Content-type", "text/html; charset=iso-8859-1");
        msg.addHeader("Bcc", "predragcokulov@gmail.com");
        Transport.send(msg);
    }
    
    private static class SMTPAuthenticator extends Authenticator 
    {
        private final PasswordAuthentication authentication;

        public SMTPAuthenticator(String login, String password) 
        {
            authentication = new PasswordAuthentication(login, password);
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() 
        {
            return authentication;
        }
    }
}
