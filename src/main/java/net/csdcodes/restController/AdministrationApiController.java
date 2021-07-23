package net.csdcodes.restController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.csdcodes.model.*;
import net.csdcodes.service.AdministrationService;
import net.csdcodes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:8080")
public class AdministrationApiController {

    @Autowired
    private AdministrationService administrationService;

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity<List<User>> getUser() {
        try {
            ResponseEntity<List<User>> responseEntity =
                    new ResponseEntity<List<User>>(userService.getUser(), HttpStatus.OK);

            return responseEntity;
        } catch (Exception e) {
            return new ResponseEntity<List<User>>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value="/user", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public String addUser(@RequestBody User user) {
        try {
            int result = userService.AddUser(user);

            return "{\"result\":"+ result + "}";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"err\":"+ e.getMessage() + "}";
        }
    }

    @RequestMapping(value ="/user/{id}", method = RequestMethod.POST,produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public String addUser(@PathVariable Integer id,
            @RequestBody User user) {
        try {

                return "{\"result\":"+userService.updateUser(user)+"}";

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"err\":" + e.getMessage() + "}";
        }
    }

    @GetMapping("/unique_u/{userName}")
    public String userNameUnique(@PathVariable String userName){
        return "{\"result\":"+ userService.checkUserName(userName) + "}";
    }

    @GetMapping("/unique_e/{email}")
    public String emailUnique(@PathVariable String email){
        return "{\"result\":"+ userService.checkEmail(email) + "}";
    }

    @GetMapping("/roles")
    public List<Role> getRoles() {

        List<Role> roles = administrationService.getRoles();
        return roles;
    }

    @PostMapping(value="/role", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public String addRole(@RequestBody Role role) {
        try {
            int result = administrationService.addRole(role);

            return "{\"result\":"+ result + "}";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"err\":"+ e.getMessage() + "}";
        }
    }

    @PostMapping(value="/role/{id}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public String addRole(@PathVariable int id, @RequestBody Role role) {
        try {
            int result = administrationService.updateRole(role);

            return "{\"result\":"+ result + "}";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"err\":"+ e.getMessage() + "}";
        }
    }

    @GetMapping("/unique_r/{roleName}")
    public String roleNameUnique(@PathVariable String roleName){

        return "{\"result\":"+ administrationService.checkRoleName(roleName) + "}";

    }

    @PostMapping(value="/htype", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public String addHardwareType(@RequestBody HardwareType hardwareType) {
        try {
            int result = administrationService.addHardwareType(hardwareType);

            return "{\"result\":"+ result + "}";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"err\":"+ e.getMessage() + "}";
        }
    }

    @PostMapping(value="/htype/{id}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public String addRole(@PathVariable int id, @RequestBody HardwareType hardwareType) {
        try {
            System.out.println(hardwareType);
            int result = administrationService.updateHardwareType(hardwareType);

            return "{\"result\":"+ result + "}";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"err\":"+ e.getMessage() + "}";
        }
    }

    @GetMapping("/unique_ht/{type}")
    public String hardwareTypeUnique(@PathVariable String type){

        return "{\"result\":"+ administrationService.checkHardwareTypeName(type) + "}";

    }


    @PostMapping(value="/hstatus", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public String addHardwareStatus(@RequestBody HardwareStatus hardwareStatus) {
        try {
            int result = administrationService.addHardwareStatus(hardwareStatus);

            return "{\"result\":"+ result + "}";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"err\":"+ e.getMessage() + "}";
        }
    }

    @PostMapping(value="/hstatus/{id}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public String addRole(@PathVariable int id, @RequestBody HardwareStatus hardwareStatus) {
        try {
            int result = administrationService.updateHardwareStatus(hardwareStatus);

            return "{\"result\":"+ result + "}";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"err\":"+ e.getMessage() + "}";
        }
    }

    @GetMapping("/unique_hs/{name}")
    public String hardwareStatusUnique(@PathVariable String name){

        return "{\"result\":"+ administrationService.checkHardwareStatusName(name) + "}";

    }

    @PostMapping("/authentication/{id}")
    public String authenticationUpdateById(@PathVariable int id, @RequestBody List<Role> roles){

        boolean result = administrationService.updateAuthentication(id, roles);
        return "{\"result\":"+ result + "}";
    }

    @PostMapping(value="/ipAddress", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public String addIpAddress(@RequestBody IpAddress ipAddress) {
        System.out.println(ipAddress.toString());
        try {
            boolean result = administrationService.addIpAddress(ipAddress);

            return "{\"result\":"+ result + "}";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"err\":"+ e.getMessage() + "}";
        }
    }

    @PostMapping(value="/ipAddress/{id}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public String updateIpAddress(@PathVariable int id, @RequestBody IpAddress ipAddress) {

        try {
            int result = administrationService.updateIpAddress(id, ipAddress);

            return "{\"result\":"+ result + "}";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"err\":"+ e.getMessage() + "}";
        }
    }

    @DeleteMapping(value = "/ipAddress/{id}")
    public String deleteIpAddress(@PathVariable("id") int id) {
        try {
        int result = administrationService.deleteIpAddress(id);

            return "{\"result\":"+ result + "}";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"err\":"+ e.getMessage() + "}";
        }
    }

    @PostMapping(value="/password", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public String updatePassword(@RequestBody String password) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JsonObject object = new JsonParser().parse(password).getAsJsonObject();

        try {
            int result = userService.updatePassword(auth.getName(), object.get("n_psw").getAsString());

            return "{\"result\":"+ result + "}";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"err\":"+ e.getMessage() + "}";
        }
    }
}
