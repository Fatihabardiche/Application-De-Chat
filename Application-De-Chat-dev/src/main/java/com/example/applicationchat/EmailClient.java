package com.example.applicationchat;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailClient {
    private static final String senderEmail = "chatapplication619@gmail.com";//change with your sender email
    private static final String senderPassword = "aqwszx123";//change with your sender password

    public static boolean sendAsHtml(String to, String password) throws Exception {
        try {
            System.out.println("Sending email to " + to);
            String title = "Reset password";

            Session session = createSession();

            //create message using session
            MimeMessage message = new MimeMessage(session);
            String htmlCode = "<h1> Your password is : </h1> <br/> <h2><b>" + password + " </b></h2>";
            message.setContent(htmlCode, "text/html");
            prepareEmailMessage(message, to, title, htmlCode);

            //sending message
            Transport.send(message);
            System.out.println("Done");
        }catch (Exception e){
            return false;
        }
        return true;
    }

    private static void prepareEmailMessage(MimeMessage message, String to, String title, String html)
            throws MessagingException {
        message.setContent(html, "text/html; charset=utf-8");
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(title);
    }

    private static Session createSession() throws Exception{
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");//Outgoing server requires authentication
        props.put("mail.smtp.starttls.enable", "true");//TLS must be activated
        props.put("mail.smtp.host", "smtp.gmail.com"); //Outgoing server (SMTP) - change it to your SMTP server
        props.put("mail.smtp.port", "587");//Outgoing port

    /*    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });*/
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        return session;
    }

}