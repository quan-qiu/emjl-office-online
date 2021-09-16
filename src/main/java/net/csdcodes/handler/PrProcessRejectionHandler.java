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
    private EmailService emailService;

    @Override
    public void execute(DelegateExecution execution) {
        String orgName = (String) execution.getVariable("orgName");
        String prTitle = (String) execution.getVariable("prTitle");
       // System.out.println("Holiday has been rejected, sending an email -----" + orgName + "---------" + prTitle);
       // emailService.sendnotification();
    }

}
