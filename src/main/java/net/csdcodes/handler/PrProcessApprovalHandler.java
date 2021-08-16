package net.csdcodes.handler;

import net.csdcodes.service.EmailService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

public class PrProcessApprovalHandler implements JavaDelegate {

    @Autowired
    EmailService emailService;

    @Override
    public void execute(DelegateExecution execution) {

        String orgName =(String) execution.getVariable("orgName");
        //System.out.println(orgName + " : Approved, sending an email");
       // emailService.sendnotification();
    }
}
