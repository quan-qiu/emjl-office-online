package net.csdcodes.restController;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.csdcodes.model.User;
import net.csdcodes.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserApiController {
    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(UserApiController.class);

    @GetMapping("/managers/{managerSsn}")
    public String getManagerNameByManagerSsns(@PathVariable String managerSsn){

        User user = userService.getUserBySsn(managerSsn);

        return user.getOrgName();
    }

}
