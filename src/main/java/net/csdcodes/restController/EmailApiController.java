package net.csdcodes.restController;

import net.csdcodes.controller.AssetController;
import net.csdcodes.model.EmailResponse;
import net.csdcodes.model.Mail;
import net.csdcodes.model.User;
import net.csdcodes.service.AsyncMailService;
import net.csdcodes.service.EmailService;
import net.csdcodes.service.PrProcessService;
import net.csdcodes.service.UserService;
import net.csdcodes.util.RequestURLUtil;
import org.apache.tomcat.util.http.ResponseUtil;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@RestController
@RequestMapping(value = "/api/email")
public class EmailApiController {

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    @Autowired
    ServletContext servletContext;

    @Autowired
    PrProcessService prProcessService;

    @RequestMapping(value = "/testSendEmail" , method = RequestMethod.GET)
    public void sendEmail(){
        emailService.sendnotification();
    }

    @Autowired
    private AsyncMailService asyncMailService;

    private final static String PR_RECEIVER = "PR-RECEIVER";

    Logger logger = LoggerFactory.getLogger(EmailApiController.class);

    @PostMapping(value="/send/mail/{prTitle}",produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> sendAsynchronousMail(@PathVariable String prTitle,
                                                       @RequestBody Mail mail){

        //logger.debug("inside sendAsynchronousMail api");
        try{
            asyncMailService.sendASynchronousMail(mail,prTitle);
        }catch (MailException e) {
            //logger.error("Exception occur while send mail :");
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e) {
            //logger.error("Exception occur while send mail :");
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity( HttpStatus.OK);
    }

    @PostMapping(value="/send/htmlmail/{prTitle}/{taskId}/{prmId}/{curtAssignee}",produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void sendAsynchronousHTMLMail(
                                    @PathVariable String prTitle,
                                    @PathVariable String taskId,
                                    @PathVariable String prmId,
                                    @PathVariable String curtAssignee,
                                    @RequestBody Mail mail, HttpServletRequest request){
        logger.debug("inside sendAsynchronousMail api");
        String to = mail.getTo();

        String urlRoot = RequestURLUtil.getSiteURL(request);

        if (to.startsWith("PR")){
            List<User> users = userService.getUserByRole(mail.getTo());
            Iterator<User> iterUsers = users.iterator();
            while (iterUsers.hasNext()){
                mail.setTo(iterUsers.next().getEmail());
                //doSendMail(prTitle,mail, url.substring(0,third),prmId);
                doSendMail(prTitle,mail, urlRoot + "/pr/prm/read/" + taskId + "/" + prmId + "/" + curtAssignee, to);
            }
            if(to.equals("PR-PURCHASER")){
                //System.out.println("-----send to receiver");
                String content = "Dear receiver, here is a link to PR.";
                ccMail(content, PR_RECEIVER, prTitle,mail, urlRoot+ "/pr/prm/history/" + prmId);

            }
        }else{
            User toUser = userService.getUserBySsn(mail.getTo());
            //System.out.println(toUser.toString());
            mail.setTo(toUser.getEmail());
            //doSendMail(prTitle,mail, url.substring(0,third),prmId);
            doSendMail(prTitle,mail, urlRoot + "/pr/prm/read/" + taskId + "/" + prmId + "/" + curtAssignee, to);
        }

    }

    @PostMapping(value="/send/htmlmail/withoutTaskId/{prTitle}/{prmId}/{curtAssignee}",produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void sendAsynchronousHTMLMailWithoutTaskId(
            @PathVariable String prTitle,
            @PathVariable String prmId,
            @PathVariable String curtAssignee,
            @RequestBody Mail mail, HttpServletRequest request){
        logger.debug("inside sendAsynchronousMailWithoutTaskId api");
        String to = mail.getTo();

        String urlRoot = RequestURLUtil.getSiteURL(request);

        if (to.equals("PR-MANAGER")){
            List<String> managerSsns = new ArrayList<String>();
            List<Task> tasks = prProcessService. getTasksByPrmId(Integer.valueOf(prmId));
            Iterator<Task> iterTask = tasks.iterator();

            while(iterTask.hasNext()){
                String taskId = iterTask.next().getId();
                Map<String,Object> variables = prProcessService.getTaskInstanceVariable(taskId);
                List<String> allManagerSsns = (List<String>) variables.get("managerSsn");
                Iterator<String> iterAllManagerSsns = allManagerSsns.iterator();
                while(iterAllManagerSsns.hasNext()){
                    String managerSsn = iterAllManagerSsns.next();
                    if (!managerSsns.contains(managerSsn)){
                        User user = userService.getUserBySsn(managerSsn);
                        mail.setTo(user.getEmail());
                        //doSendMail(prTitle,mail, url.substring(0,third),prmId);
                        doSendMail(prTitle,mail, urlRoot + "/pr/prm/read/" + taskId + "/"  + prmId + "/" + curtAssignee, to);
                        managerSsns.add(managerSsn);
                        break;
                    }

                }
            }

        }else if(to.equals("PR-PURCHASER")){
            //System.out.println("-----send to receiver");
            String content = "Dear receiver, here is a link to PR.";
            ccMail(content, PR_RECEIVER, prTitle,mail, urlRoot+ "/pr/prm/history/" + prmId);

        }else{
            Task task = prProcessService.getTaskByPrmId(Integer.valueOf(prmId));
            List<User> users = userService.getUserByRole(mail.getTo());
            Iterator<User> iterUsers = users.iterator();
            while (iterUsers.hasNext()){
                mail.setTo(iterUsers.next().getEmail());
                //doSendMail(prTitle,mail, url.substring(0,third),prmId);
                doSendMail(prTitle,mail, urlRoot + "/pr/prm/read/" + task.getId() + "/"  + prmId + "/" + curtAssignee, to);
            }
        }
    }

    private void ccMail(String content, String mailGroup, String prTitle,Mail mail, String url){
        List<User> receivers = userService.getUserByRole(mailGroup);
        Iterator<User> iterReceivers = receivers.iterator();
        while (iterReceivers.hasNext()){
            mail.setTo(iterReceivers.next().getEmail());
            mail.setContent(content);
            //System.out.println(mail.toString());
            //doSendMail(prTitle,mail, url.substring(0,third),prmId);
            doSendMail(prTitle,mail, url, PR_RECEIVER);
        }
    }

    private void doSendMail(String prTitle, Mail mail,String path, String to){
        //System.out.println("---------path : "  + path);
        try{
            mail.setSubject("Notification from PR process administrator");
            Map<String, String> model = new HashMap<String, String>();
            model.put("prTitle", prTitle);
            model.put("mailContent", mail.getContent());
            model.put("conPath",path);
            //model.put("prmId", prmId);
            model.put("to", to);

            mail.setModel(model);

            asyncMailService.sendASynchronousHTMLMail(mail);
        }catch (MailException e) {
            logger.error("Exception occur while send mail :");
            //return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e) {
            logger.error("Exception occur while send mail :");
            //return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //return new ResponseEntity( HttpStatus.OK);
    }


}
