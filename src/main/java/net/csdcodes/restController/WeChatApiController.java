package net.csdcodes.restController;

import com.google.gson.Gson;
import net.csdcodes.component.WechatApiComponent;
import net.csdcodes.model.PersonalMessage;
import net.csdcodes.model.User;
import net.csdcodes.model.WeChatUrlData;
import net.csdcodes.service.WeChatMsgSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wechat")
@CrossOrigin(origins = "http://localhost:8080")
public class WeChatApiController {


    @PostMapping(value="/message", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public String buildMsg() {
        WeChatMsgSendService weChatMsgSendService = new WeChatMsgSendService();

        try {
            String token = weChatMsgSendService.getToken("ww4f77ff551a51c6cc","Mw5otFib2Qa_RdSOmP_ri6yHqBKQtJJ8ULMoeIpZytA");
            String postData = weChatMsgSendService.createpostdata ("2009-1736", "text", 1000016, "content", "this is a test information");
            String resp = weChatMsgSendService.post("utf-8", weChatMsgSendService.CONTENT_TYPE,(new WeChatUrlData()).getSendMessage_Url(), postData, token);
            System.out.println ("Get token =======>" + token);
            System.out.println ("request data =======>" + postData);
            System.out.println ("Send WeChat Response Data =======>" + resp);
        }catch (Exception e) {
            System.out.println(e.getStackTrace());
            System.out.println(e.getMessage());
        }
        return "true";
    }
}
