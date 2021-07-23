package net.csdcodes.handler;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class PrProcessApprovalHandler implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {

        String orgName =(String) execution.getVariable("orgName");
        System.out.println("Approved, sending an email");
    }
}
