package net.csdcodes.handler;

import net.csdcodes.service.EmailService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class PrProcessRejectionHandler implements JavaDelegate {


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

    @Override
    public void execute(DelegateExecution execution) {
        String orgName = (String) execution.getVariable("orgName");
        String prTitle = (String) execution.getVariable("prTitle");
        //System.out.println("Holiday has been rejected, sending an email -----" + orgName + "---------" + prTitle);
        //sendnotification();
    }

}
