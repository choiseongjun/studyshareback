package study.share.com.source.service;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class MailService {

    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "noreply@studyshare.com";

    public void sendMail(String email, String title, String content) throws AddressException, MessagingException {
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setTo(email);
//        simpleMailMessage.setFrom(MailService.FROM_ADDRESS);
//        simpleMailMessage.setSubject(title);
//        simpleMailMessage.setText(content);
        
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        mimeMessage.setFrom(new InternetAddress(MailService.FROM_ADDRESS));
        mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(email));
        mimeMessage.setSubject(title);
        mimeMessage.setText(content,"UTF-8","html");
         
        mailSender.send(mimeMessage);
    }
}