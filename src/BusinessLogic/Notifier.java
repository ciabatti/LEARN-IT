package BusinessLogic;

import DomainModel.Trainer;
import DomainModel.Company;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.ArrayList;
import java.util.Properties;

public class Notifier{
    private static Notifier notifier;

    private final String emailAddress = "randomemail@gmail.com";
    private final String emailPassword = "randompassword";
    private Notifier(){}
    public static synchronized Notifier getInstance(){
        if(notifier == null){
            notifier = new Notifier();
        }
        return notifier;
    }

    public void sendEmailCompany(ArrayList<Company> receivers, String subject, String tempMessage) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");  //autenticazione user
        properties.put("mail.smtp.host", "smtp.gmail.com");  //server smtp gmail
        properties.put("mail.smtp.port", "587"); //numero di porta richiesto da gmail
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAddress, emailPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailAddress));
        message.setSubject(subject);

        MimeMultipart multipart = new MimeMultipart("related");

        for (Company company : receivers) {
            Address addressTo = new InternetAddress(company.getEmail());
            message.setRecipient(Message.RecipientType.TO, addressTo);
            String htmlText = "<img src=\"cid:image\" alt=\"img\" style=\"width: 150px; height: 150px; \">\r\n" + //
                    "<h3 style=\"color: black; font-family: Arial,sans-serif\">" + "Hi " + company.getName()+ ", " + tempMessage + "</h3>\r\n";
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);

            BodyPart messageBodyPart1 = new MimeBodyPart();
            FileDataSource fds = new FileDataSource("images/logo.png");
            messageBodyPart1.setDataHandler(new DataHandler(fds));
            messageBodyPart1.setHeader("Content-ID", "<image>");
            messageBodyPart1.setFileName("logo.png");
            multipart.addBodyPart(messageBodyPart1);
            message.setContent(multipart);
            Transport.send(message);
            multipart.removeBodyPart(messageBodyPart);
            multipart.removeBodyPart(messageBodyPart1);
        }
    }
    public void sendEmailTrainer(ArrayList<Trainer> receivers, String subject, String tempMessage) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");  //autenticazione user
        properties.put("mail.smtp.host", "smtp.gmail.com");  //server smtp gmail
        properties.put("mail.smtp.port", "587"); //numero di porta richiesto da gmail
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAddress, emailPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailAddress));
        message.setSubject(subject);
        MimeMultipart multipart = new MimeMultipart("related");

        for (Trainer trainer : receivers) {
            Address addressTo = new InternetAddress(trainer.getEmail());
            message.setRecipient(Message.RecipientType.TO, addressTo);
            String htmlText = "<img src=\"cid:image\" alt=\"\" style=\"width: 150px; height: 150px; \">\r\n" + //
                    "<h3 style=\"color: black; font-family: Arial,sans-serif\">" + "Hi " + trainer.getName()+ ", " + tempMessage + "</h3>\r\n";
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);

            BodyPart messageBodyPart1 = new MimeBodyPart();
            FileDataSource fds = new FileDataSource("imgs/Logo.png");
            messageBodyPart1.setDataHandler(new DataHandler(fds));
            messageBodyPart1.setHeader("Content-ID", "<image>");
            messageBodyPart1.setFileName("Logo.png");
            multipart.addBodyPart(messageBodyPart1);
            message.setContent(multipart);
            Transport.send(message);
            multipart.removeBodyPart(messageBodyPart);
            multipart.removeBodyPart(messageBodyPart1);
        }
    }

}
