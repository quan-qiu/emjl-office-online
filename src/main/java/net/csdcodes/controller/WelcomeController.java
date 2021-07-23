package net.csdcodes.controller;

import net.csdcodes.model.User;
import net.csdcodes.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.core.env.Environment;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * creator: Quan Qiu
 * date: 06/03/21
 */
@Controller
public class WelcomeController {

    Logger logger = LoggerFactory.getLogger(WelcomeController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RequestService requestService;

    private String appMode;

    @Autowired
    public WelcomeController(Environment environment){
        appMode = environment.getProperty("app-mode");

    }

    @GetMapping("/")
    private String index(HttpServletRequest request,Model model){
        String clientIp = requestService.getClientIp(request);
        model.addAttribute("clientIp", clientIp);

        return "index";
    }

    @GetMapping("/welcome")
    private String welcome(){
        return "welcome";
    }

    @GetMapping("/hello")
    private String hello(Model model){
        String sql = "select id,username,password from [dbo].[user]";
        List users = jdbcTemplate.query(sql,BeanPropertyRowMapper.newInstance(User.class));
        users.forEach(System.out :: println);
        model.addAttribute("users",users);
        return "hello";
    }

    @GetMapping("/calendar")
    private String calendar(Model model){
        return "calendar";
    }

    @GetMapping("/403")
    public String error403(){
        return "403";
    }

   /* @GetMapping("/login")
    public String  loginPage(){
        return "login";
    }*/
}
