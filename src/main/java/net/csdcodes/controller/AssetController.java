package net.csdcodes.controller;

import net.csdcodes.model.Asset;
import net.csdcodes.model.User;
import net.csdcodes.service.AssetService;
import net.csdcodes.service.RequestService;
import net.csdcodes.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * creator: Quan Qiu
 * date: 06/03/21
 */
@Controller
@RequestMapping("/it-asset")
public class AssetController {

    Logger logger = LoggerFactory.getLogger(AssetController.class);

    @Value("${page.size}")
    private int pageSize;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RequestService requestService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private UserService userService;

    @GetMapping("/index")
    private String index(HttpServletRequest request,Model model){

        String clientIp = requestService.getClientIp(request);
        model.addAttribute("clientIp", clientIp);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
/*        model.addAttribute("loggedinuser", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());*/
        //System.out.println(authentication.getName());
        //System.out.println(authentication.getAuthorities());

        return "it-asset/index";
    }
    @ModelAttribute("loggedinuser")
    public User globalUserObject(Model model) {
        // Add all null check and authentication check before using. Because this is global
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedinuser", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());
        // Create User pojo class
        //User user = new User(authentication.getName(), Arrays.asList(authentication.getAuthorities()));
        return null;
    }

    @GetMapping(value="/row/{name}/{classify}/{status}")
    private String getAssetRows(@PathVariable(name="name", required = false) String name,
                                @PathVariable(name="classify", required = false) String classify,
                                @PathVariable(name="status", required = false) String status,
                                Model model){
        return getAssetByPage(name,classify,status,1,model);
    }

    @GetMapping(value = "/row/{name}/{classify}/{status}/{pageNo}")
    public String getAssetByPage(@PathVariable(name="name", required = false) String name,
                                 @PathVariable(name="classify", required = false) String classify,
                                 @PathVariable(name="status", required = false) String status,
                                 @PathVariable(name="pageNo", required = false) int pageNo,
                                 Model model){

        try {

            Page<Asset> page = assetService.getAssetByPage(pageNo,pageSize,name,classify,status);
            List<Asset> listAssets = page.getContent();
            model.addAttribute("assets",listAssets);
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("name",name);
            model.addAttribute("classify", classify);
            model.addAttribute("status", status);
        }catch (Exception e){
            ResponseEntity<List<Asset>> responseEntity =new ResponseEntity<List<Asset>>(HttpStatus.BAD_REQUEST);
        }

        return "it-asset/asset_list";

    }

    @GetMapping(value="/row-p")
    private String getAssetRows(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User thisUser = userService.getUserByUsername(auth.getName());

        try {
            List assets = assetService.getAsset(thisUser.getSsn());
            //int count = assetService.getAssetCount(name,classify,status);
            model.addAttribute("assets",assets);
            //model.addAttribute("count",count);

        }catch (Exception e){
            ResponseEntity<List<Asset>> responseEntity =new ResponseEntity<List<Asset>>(HttpStatus.BAD_REQUEST);
        }

        return "it-asset/asset_list_p";
    }

    @GetMapping("/new")
    public String showNewAssetPage(Model model){
        Asset asset = new Asset();
        model.addAttribute("asset", asset);

        return "it-asset/asset_new";
    }

    @PostMapping(value="/save")
    public String addAsset(@ModelAttribute("asset") Asset asset){
        int id = 0;
        try{
            //System.out.println(asset.toString());
            id = assetService.save(asset);
            ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);

            //System.out.println(id);

        }catch (Exception e){
            ResponseEntity responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
            System.out.println(e.getMessage());

        }

        return "redirect:/it-asset/asset/" + Long.toString(id);
    }

    @GetMapping(value = "/asset/{id}")
    public String getAssetById(@PathVariable("id") int id, Model model){
        try{
            Asset asset = assetService.getAssetById(id);
            model.addAttribute("asset",asset);
            ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);

        }catch (Exception e){
            ResponseEntity responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
            System.out.println(e.getMessage());
            //return "";
            //return responseEntity;
        }

        return "it-asset/asset_edit";
    }

/*    @PostMapping(value = "/asset")
    public String updateAsset(@ModelAttribute("asset") Asset asset) {
        try{
            int result = assetService.update(asset);
            ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
        }catch (Exception e){
            ResponseEntity responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return "redirect:/it-asset/index";
    }*/

    @DeleteMapping(value ="/delete/{id}")
    public String deleteAsset(@PathVariable("id") String id){
        try{
            int result = assetService.deleteAsset(Integer.parseInt(id));
            ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
            //return "redirect:/it-asset/index";
            //return responseEntity;
        }catch (Exception e){
            ResponseEntity responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
            System.out.println(e.getMessage());
            //return "";
            //return responseEntity;
        }

        return "it-asset/index";
    }
}
