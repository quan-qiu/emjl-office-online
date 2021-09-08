package net.csdcodes.controller;

import net.csdcodes.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 * creator: Quan Qiu
 * date: 06/03/21
 */
@Controller
@RequestMapping("/hr")
public class HrController {

    Logger logger = LoggerFactory.getLogger(HrController.class);

    @Autowired
    FileService fileService;

    @GetMapping(value = "/pay/upload")
    public String uploadPayFile(){

        return "hr/upload";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        fileService.uploadFile(file);

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @GetMapping("/")
    public String index() {
        return "/hr/upload";
    }
}
