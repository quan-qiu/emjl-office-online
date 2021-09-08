package net.csdcodes.controller;


import net.csdcodes.service.StorageService;
import net.csdcodes.service.exception.StorageFileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Collectors;
/**
 * creator: Quan Qiu
 * date: 06/03/21
 */
@Controller
@RequestMapping("/file")
public class FileUploadController {

    Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Value("${app.upload.root-dir}")
    private String rootDir;
    @Value("${app.upload.file-root-dir}")
    private String location;
    @Value("${app.upload.pr-location-dir}")
    private String prLocation;
    @Value("${app.upload.prd-template-dir}")
    private String prdTemplateLocation;
    @Value("${app.upload.prd-upload-dir}")
    private String prdUploadLocation;


    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{dirtype}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String dirtype, @PathVariable String filename) {
        Resource file = null;

        switch (dirtype) {
            case "PR_PRD_TEMPLATE":
                //System.out.println("----- PR_PRD_TEMPLATE:" + Paths.get(rootDir + prdTemplateLocation));
                file = storageService.loadAsResource(filename, Paths.get(rootDir + prdTemplateLocation));
                break;
            case "PR_PRD_UPLOAD":
                //System.out.println(Paths.get(rootDir + prdUploadLocation).toString());
                file = storageService.loadAsResource(filename, Paths.get(rootDir + prdUploadLocation));
                break;
        }
        //System.out.println(file.getFilename());
        //Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
