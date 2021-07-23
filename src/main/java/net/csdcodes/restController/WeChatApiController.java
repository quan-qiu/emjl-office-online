package net.csdcodes.restController;

import com.google.gson.Gson;
import net.csdcodes.component.WechatApiComponent;
import net.csdcodes.model.PersonalMessage;
import net.csdcodes.model.User;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wechat")
@CrossOrigin(origins = "http://localhost:8080")
public class WeChatApiController {

    @PostMapping(value="/message", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public String buildMsg() {

        PersonalMessage personalMessage = new PersonalMessage();

        WechatApiComponent wechatApiComponent = new WechatApiComponent();
        wechatApiComponent.setAccessToken();
        System.out.println(wechatApiComponent.getAccessToken());

        personalMessage.setAgentId(wechatApiComponent.getAGENTID());
        personalMessage.setMsgType("text");
        personalMessage.setToUser("2009-1736");
        personalMessage.setText("this is a test");
        personalMessage.setDuplicate_check_interval(1800);
        personalMessage.setEnable_id_trans(0);
        personalMessage.setEnable_duplicate_check(0);
        personalMessage.setSafe(0);

        System.out.println(personalMessage.toString());

        return wechatApiComponent.sendMesToPersonal(wechatApiComponent.buildMessageBody(personalMessage));
    }
}
