package net.csdcodes.restController;

import net.csdcodes.controller.AssetController;
import net.csdcodes.model.EmailResponse;
import net.csdcodes.model.Mail;
import net.csdcodes.model.User;
import net.csdcodes.service.AsyncMailService;
import net.csdcodes.service.EmailService;
import net.csdcodes.service.UserService;
import org.apache.tomcat.util.http.ResponseUtil;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/email")
public class EmailApiController {

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    @Autowired
    ServletContext servletContext;

    @RequestMapping(value = "/testSendEmail" , method = RequestMethod.GET)
    public void sendEmail(){
        emailService.sendnotification();
    }

    @Autowired
    private AsyncMailService asyncMailService;

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

    @PostMapping(value="/send/htmlmail/{prTitle}/{prmId}/{curtAssignee}",produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void sendAsynchronousHTMLMail(@PathVariable String prTitle,
                                         @PathVariable String prmId,
                                         @PathVariable String curtAssignee,
                                         @RequestBody Mail mail, HttpServletRequest request){
        logger.debug("inside sendAsynchronousMail api");
        String to = mail.getTo();

        String url = request.getRequestURL().toString();
        int first = url.indexOf("/");
        int second = url.indexOf("/",first + 1);
        int third = url.indexOf("/", second + 1);

        if (to.startsWith("PR")){
            List<User> users = userService.getUserByRole(mail.getTo());
            Iterator<User> iterUsers = users.iterator();
            while (iterUsers.hasNext()){
                mail.setTo(iterUsers.next().getEmail());
                doSendMail(prTitle,mail, url.substring(0,third),prmId);
            }
        }else{
            User toUser = userService.getUserBySsn(mail.getTo());
            System.out.println(toUser.toString());
            mail.setTo(toUser.getEmail());
            doSendMail(prTitle,mail, url.substring(0,third),prmId);
        }

    }

    private void doSendMail(String prTitle, Mail mail,String path, String prmId){
        try{
            mail.setSubject("Notification from PR process administrator");
            Map model = new HashMap();
            model.put("prTitle", prTitle);
            model.put("mailContent", mail.getContent());
            model.put("conPath",path);
            model.put("prmId", prmId);

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
