package net.csdcodes.service;


import com.sun.mail.util.MailSSLSocketFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    /*@Value("${email.from.address}")
    private String fromAddress;*/

    public void sendnotification() throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("quan.qiu@euro-misi.cn");
        mail.setFrom("euro-misi-it@euro-misi.cn");
        mail.setSubject("test");
        mail.setText("test");

        javaMailSender.send(mail);
    }

    /*public void sendMailMultipart(String toEmail, String subject, String message, File file) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(fromAddress);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(message);

        if(file != null){
            helper.addAttachment(file.getName(), file);
        }
        javaMailSender.send(mimeMessage);
    }

    public void sendMail(String toEmail, String subject, String message) throws MessagingException {
        sendMailMultipart(toEmail, subject, message, null);
    }

    public void sendMail(String toEmail, String subject, String message, File file) throws MessagingException {
        sendMailMultipart(toEmail, subject, message, file);
    }*/



}
