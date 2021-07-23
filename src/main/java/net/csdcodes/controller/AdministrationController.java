package net.csdcodes.controller;

import net.csdcodes.model.*;
import net.csdcodes.service.AdministrationService;
import net.csdcodes.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdministrationController {

    Logger logger = LoggerFactory.getLogger(AdministrationController.class);

    @Value("${page.size}")
    private int pageSize;

    @Autowired
    private AdministrationService administrationService;
    @Autowired
    private UserService userService;

    @GetMapping("")
    private String index(HttpServletRequest request, Model model){
        return "administration/index";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {

            List<User> users = userService.getUser();
            model.addAttribute("users",users);

        return "administration/user_list";
    }

    @GetMapping("/user")
    public String getUser(Model model) {

        User user = new User();
        model.addAttribute("user",user);

        return "administration/user_new";
    }

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable Integer id, Model model) {

        User user = userService.getUserById(id);
        model.addAttribute("user",user);

        return "administration/user_edit";
    }

    @GetMapping("/roles")
    public String getRoles(Model model) {

        List<Role> roles = administrationService.getRoles();
        model.addAttribute("roles",roles);

        return "administration/role_list";
    }

    @GetMapping("/role")
    public String getRole(Model model) {

        Role role = new Role();
        model.addAttribute("role",role);

        return "administration/role_new";
    }

    @GetMapping("/role/{id}")
    public String getRole(@PathVariable Integer id, Model model) {

        Role role = administrationService.getRoleById(id);
        model.addAttribute("role",role);

        return "administration/role_edit";
    }

    @GetMapping("/password")
    public String getPassword( Model model) {

        return "administration/password_update";
    }

    @GetMapping("/htypes")
    public String getHardwareTypes(Model model) {

        List<HardwareType> hardwareTypes = administrationService.getHardwareTypes();
        model.addAttribute("htypes",hardwareTypes);

        return "administration/hardwareType_list";
    }

    @GetMapping("/htype")
    public String getHardwareType(Model model) {

        HardwareType hardwareType = new HardwareType();
        model.addAttribute("hardwareType",hardwareType);

        return "administration/hardwareType_new";
    }

    @GetMapping("/htype/{id}")
    public String getHardwareType(@PathVariable Integer id, Model model) {

        HardwareType hardwareType  = administrationService.getHardwareTypeById(id);
        model.addAttribute("hardwareType",hardwareType);

        return "administration/hardwareType_edit";
    }

    @GetMapping("/hstatuses")
    public String getHardwareStatuses(Model model) {

        List<HardwareStatus> hardwareStatuses = administrationService.getHardwareStatuses();
        model.addAttribute("hstatuses",hardwareStatuses);

        return "administration/hardwareStatus_list";
    }

    @GetMapping("/hstatus")
    public String getHardwareStatus(Model model) {

        HardwareStatus hardwareStatus = new HardwareStatus();
        model.addAttribute("hardwareStatus",hardwareStatus);

        return "administration/hardwareStatus_new";
    }

    @GetMapping("/hstatus/{id}")
    public String getHardwareStatus(@PathVariable int id, Model model) {

        HardwareStatus hardwareStatus  = administrationService.getHardwareStatusById(id);
        model.addAttribute("hardwareStatus",hardwareStatus);

        return "administration/hardwareStatus_edit";
    }

    @GetMapping("/authentications")
    public String getUserRoles(Model model) {

        List<User> userRoles = administrationService.getUserRoles();
        model.addAttribute("userRoles",userRoles);

        return "administration/authentication_list";
    }

    @GetMapping("/authentication/{id}")
    public String getAuthentication(@PathVariable int id, Model model) {

        User user  = userService.getUserById(id);
        Set<Role> urole = userService.getUserRoles(id);
        List<Role> roles= administrationService.getRoles();
        model.addAttribute("user",user);
        model.addAttribute("urole",urole);
        model.addAttribute("roles",roles);
        return "administration/authentication_edit";
    }


    @GetMapping("/ipAddresses")
    public String getIpAddressed(Model model) {

        return getAssetByPage(1,model);
        /*List<IpAddress> ipAddresses = administrationService.getIpAddresses();
        model.addAttribute("ipAddresses",ipAddresses);

        return "administration/ipAddress_list";*/
    }

    @GetMapping(value = "/ipAddresses/{pageNo}")
    public String getAssetByPage(@PathVariable(name="pageNo") int pageNo,
                                 Model model){

        try {

            Page<IpAddress> page = administrationService.getIpAddressByPage(pageNo, pageSize);
            List<IpAddress> listIpAddress = page.getContent();
            model.addAttribute("ipAddresses",listIpAddress);
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());

        }catch (Exception e){
            ResponseEntity<List<Asset>> responseEntity =new ResponseEntity<List<Asset>>(HttpStatus.BAD_REQUEST);
        }

        return "administration/ipAddress_list";

    }

    @GetMapping("/ipAddress")
    public String getIpAddress(Model model) {

        IpAddress ipAddress = new IpAddress();
        model.addAttribute("ipAddress",ipAddress);

        return "administration/ipAddress_new";
    }

    @GetMapping("/ipAddress/{id}")
    public String getIpAddress(@PathVariable int id, Model model) {

        IpAddress ipAddress  =  administrationService.getIpAddressById(id);
        model.addAttribute("ipAddress",ipAddress);
        return "administration/ipAddress_edit";
    }

    @GetMapping("/deptEmp")
    public String getDept(Model model) {

        return "administration/dept_emp";
    }
}
