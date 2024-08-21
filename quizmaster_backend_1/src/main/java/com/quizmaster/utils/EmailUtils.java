package com.quizmaster.utils;

import com.quizmaster.configurations.EmailConfigs;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class EmailUtils {

    public static void sendVerificationCode(String emailAddress, String verificationCode) {
        String subject="Lendr Verification Code";
        String message=generateHtmlMessage(verificationCode);
//        " Your Lendr verification code is "+verificationCode+" . Do not share it with anyone. The verification code is valid for 30 minutes. ";
//
        sendEmail(subject,message,emailAddress);
    }


    public static MimeMessage createMimeMessage(Session session, String recipientEmail, String subject, String message) throws MessagingException {
        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(EmailConfigs.email));
            if(!recipientEmail.isEmpty())
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            mimeMessage.setSubject(subject);



            Multipart multipart=new MimeMultipart("mixed");

            MimeBodyPart textPart = new MimeBodyPart();
//            textPart.s
            textPart.setContent(message,"text/html");
            // Add text part to the multipart
            multipart.addBodyPart(textPart);
            mimeMessage.setContent(multipart);

            return mimeMessage;
        } catch (Exception e) {
            e.printStackTrace();
            //throw new MessagingException("Error creating MimeMessage", e);
        }
        return null;
    }

    public static Properties getProperties()
    {

        System.out.println("getting Email properties ");
        // Create email properties
        Properties properties = new Properties();

        properties.put("mail.smtp.host", EmailConfigs.server);
        properties.put("mail.smtp.port", EmailConfigs.port);
//        properties.put("mail.smtp.auth", "true");

        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");


        System.out.println("getting Email encryptions start");

        return properties;
    }

    public static void sendEmail(String subject, String message, String recipientEmail) {

        if(recipientEmail==null||recipientEmail.isEmpty())
            return;

        try {
            Properties properties = getProperties();
            properties.put("mail.debug", "false");
            System.out.println("Email properties gotten successfully");

            // Create a session with authentication
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EmailConfigs.email, EmailConfigs.password);
                }
            });

            try {
                MimeMessage mimeMessage=createMimeMessage(session,recipientEmail,subject,message);

                Transport.send(mimeMessage);

                // Email sent successfully, you can log this or perform other actions
                System.out.println("Email Sent Successfully");
            } catch (Exception e) {
                System.out.println("error Sending Email "+e);
                e.printStackTrace();
                // Handle email sending failure
            }
        }catch (Exception erd)
        {
            System.out.println("Email error "+erd);
            erd.printStackTrace();
        }
    }

    private static String generateHtmlMessage(String verificationCode) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<!DOCTYPE html>\n");
        htmlBuilder.append("<html lang=\"en\">\n");
        htmlBuilder.append("<head>\n");
        htmlBuilder.append("    <meta charset=\"UTF-8\">\n");
        htmlBuilder.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        htmlBuilder.append("    <title>QuizMaster Verification Code</title>\n");
        htmlBuilder.append("    <style>\n");
        htmlBuilder.append("        body {\n");
        htmlBuilder.append("            font-family: Arial, sans-serif;\n");
        htmlBuilder.append("            margin: 20px;\n");
        htmlBuilder.append("        }\n");
        htmlBuilder.append("\n");
        htmlBuilder.append("        .message-container {\n");
        htmlBuilder.append("            background-color: #f0f0f0;\n");
        htmlBuilder.append("            padding: 20px;\n");
        htmlBuilder.append("            border-radius: 8px;\n");
        htmlBuilder.append("            margin-bottom: 20px;\n");
        htmlBuilder.append("        }\n");
        htmlBuilder.append("    </style>\n");
        htmlBuilder.append("</head>\n");
        htmlBuilder.append("<body>\n");
        htmlBuilder.append("\n");
        htmlBuilder.append("<div class=\"message-container\">\n");
        htmlBuilder.append("    <p>Your Quizmaster verification code is <strong>").append(verificationCode).append("</strong>.<br>\n");
        htmlBuilder.append("        Do not share it with anyone. The verification code is valid for 30 minutes.</p>\n");
        htmlBuilder.append("</div>\n");
        htmlBuilder.append("\n");
        htmlBuilder.append("</body>\n");
        htmlBuilder.append("</html>");

        return htmlBuilder.toString();
    }


}
