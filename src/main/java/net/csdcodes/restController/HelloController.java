package net.csdcodes.restController;

import net.csdcodes.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return  "Hello world restful with Spring Boot";
    }

    @RequestMapping("/param")
    public String helloParam(@RequestParam(name="name",defaultValue="world") String name){
        return "Hello " + name;
    }

    @PostMapping("/hello")
    public String add(){
        return "response you have sent POST request";
    }

    @PutMapping("/hello")
    public String update(){
        return "response you have sent PUT request";
    }

    @DeleteMapping("/hello")
    public String delete(){
        return "response you have sent DELETE request";
    }

/*    @GetMapping("/getUser")
    //@GetMapping(value="/getUser",consumes={MidiaType.TEXT_HTML_VALUE}) //在HTMLheader里  Content-Type:text/html
    //@GetMapping(value="/getUser",produces={MidiaType.TEXT_HTML_VALUE}) //返回HTML return "<html><body>rest spring boot<body></html>";
    public User getUser(){
        return new User(2,"qiu","quan");
    }

    @GetMapping("/getUsers")
    public List<User> getUsers(){
        List<User> users = new ArrayList<User>();
        users.add(new User(2,"qiu","quan"));
        users.add(new User(2,"qiu1","quan1"));
        return users;
    }*/

/*    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id){
        if (id==3){
            User user = new User(3,"qiu3","quan3");
            return  new ResponseEntity<User>(user, HttpStatus.OK);
        }
        return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
    }*/



}
