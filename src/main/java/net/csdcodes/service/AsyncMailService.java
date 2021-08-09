package net.csdcodes.service;

import net.csdcodes.model.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class AsyncMailService {
    @Autowired
    private JavaMailSender sender;

    @Autowired
    private TemplateEngine templateEngine;

    private static final Logger logger = LoggerFactory.getLogger(AsyncMailService.class);

    @Value("spring.mail.username")
    private static String mailSender;

    public AsyncMailService(){}

    public static int noOfQuickServiceThreads = 20;


    /**
     * this statement create a thread pool of twenty threads
     * here we are assigning send mail task using ScheduledExecutorService.submit();
     */
    private ScheduledExecutorService quickService = Executors.newScheduledThreadPool(noOfQuickServiceThreads); // Creates a thread pool that reuses fixed number of threads(as specified by noOfThreads in this case).

    public void sendASynchronousMail(Mail inMail, String prTitle) throws MailException,RuntimeException{
        logger.debug("inside sendASynchronousMail method");
        SimpleMailMessage mail=new SimpleMailMessage();
        mail.setFrom(inMail.getFrom());
        mail.setTo(inMail.getTo());
        mail.setSubject(inMail.getSubject()+" (Do Not Reply To This Email)");
        mail.setText("This an alert email from service : "+inMail.getContent());
        quickService.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    sender.send(mail);
                }catch(Exception e){
                    logger.error("Exception occur while send a mail : ",e);
                }
            }
        });
    }

    public void sendASynchronousHTMLMail(Mail mail) throws MailException, RuntimeException, MessagingException {
        logger.debug("inside sendASynchronousHTMLMail method");

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();
        Map<String, String> vars= (Map<String, String>)mail.getModel();
        context.setVariables(Collections.<String, Object>unmodifiableMap(vars));

        String html = templateEngine.process("email/pr_alert_email", context);

        helper.setFrom(mail.getFrom());
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject()+" (Do Not Reply To This Email)");
        helper.setText(html,true);
        quickService.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    sender.send(message);
                }catch(Exception e){
                    logger.error("Exception occur while send a mail : ",e);
                }
            }
        });
    }
}
