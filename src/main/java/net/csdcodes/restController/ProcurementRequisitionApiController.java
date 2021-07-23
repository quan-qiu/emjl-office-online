package net.csdcodes.restController;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.csdcodes.controller.FileUploadController;
import net.csdcodes.model.*;
import net.csdcodes.model.ls.PrUploadFileDirEnum;

import net.csdcodes.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

@RestController
@RequestMapping("/api/pr")
@CrossOrigin(origins = "http://localhost:8080")
public class ProcurementRequisitionApiController {

    @Autowired
    ProcurementRequisitionService prs;
    @Autowired
    UserService userService;
    @Autowired
    FileSystemStorageService storageService;
    @Autowired
    PrProcessService prProcessService;

    protected static final String TASK_MANAGER_DIRECTOR_GROUP = "PR-MANAGING-DIRECTOR";

    @PostMapping(value="/prm/add/flow_type", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public String addFlowType(@RequestBody String flowType) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User thisUser = userService.getUserByUsername(auth.getName());

        JsonElement flowTypeJE = new JsonParser().parse(flowType);
        JsonObject flowTypeObject = flowTypeJE.getAsJsonObject();

        try {
            int newRowId = prs.addFlowType(thisUser,
                    flowTypeObject);

            return "{\"newRowId\":"+ newRowId + "}";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"err\":"+ e.getMessage() + "}";
        }
    }

    @PostMapping(value="/prm/update/{id}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public String updatePRMainById(@PathVariable int id, @RequestBody ProcurementRequisitionMain prm) {
        System.out.println(prm);
        try {
            int result = prs.updatePRMainById(id,prm);

            return "{\"result\":"+ result + "}";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"err\":"+ e.getMessage() + "}";
        }
    }

    @PostMapping(value="/prd/new/{prmId}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public String createPRDetail(@PathVariable int prmId, @RequestBody ProcurementRequisitionDetail prd) {
        try {
            int result = prs.createPRDetail(prmId, prd);

            return "{\"result\":"+ result + "}";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"err\":"+ e.getMessage() + "}";
        }
    }

    @PostMapping(value="/prd/{prdId}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public String updatePRDetail(@PathVariable int prdId, @RequestBody ProcurementRequisitionDetail prd) {
        try {
            int result = prs.updatePRDetailById(prdId, prd);

            return "{\"result\":"+ result + "}";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"err\":"+ e.getMessage() + "}";
        }
    }

    @DeleteMapping(value = "prd/{prdId}")
    public String deletePRDetail(@PathVariable int prdId) {
        try {
            int result = prs.deletePRDetailById(prdId);

            return "{\"result\":"+ result + "}";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"err\":"+ e.getMessage() + "}";
        }
    }

    @DeleteMapping(value = "prm/{prmId}")
    public String deletePRM(@PathVariable int prmId){
        try {
            int result = prs.deletePRM(prmId);

            return "{\"result\":"+ result + "}";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "{\"err\":"+ e.getMessage() + "}";
        }
    }

    @PostMapping("/prd")
    public UploadFileResponse handlePrdUpload(@RequestParam("file") MultipartFile file) {

        storageService.storeToDir(file, PrUploadFileDirEnum.PR_PRD_UPLOAD.name().toString());

        String url = MvcUriComponentsBuilder
                .fromMethodName(FileUploadController.class, "serveFile" ,
                        PrUploadFileDirEnum.PR_PRD_UPLOAD.name().toString(),
                        file.getOriginalFilename().toString())
                .build()
                .toString();

        return new UploadFileResponse(file.getOriginalFilename(), url,
                file.getContentType(), file.getSize());

    }

    @PostMapping("/prd/loadFromFile/{filename:.+}/{prmId}")
    public String loadPRDFromFile(@PathVariable String filename, @PathVariable int prmId) {

        try{
            //storageService.loadPRDFromFile(filename, prmId);
            storageService.ApachePOIExcelReadPRDFile(filename,prmId);
        }catch (IOException | ParseException e){
            System.out.println(e.getStackTrace());
        }

        return null;

    }

    @PostMapping("/prc")
    public ResponseEntity<?> addProcurementRequisitionComment(@RequestBody ProcurementRequisitionComment prc) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User thisUser = userService.getUserByUsername(auth.getName());
        int result = prs.addProcurementRequisitionComment(prc, thisUser);

        return ResponseEntity.noContent().build();

    }

    @PostMapping("/prm/flowStatus/{prmId}/{approved}/{role}")
    public ResponseEntity<?> updatePrmFlowStatus(@PathVariable int prmId, @PathVariable int approved, @PathVariable String role){
        if (approved == 1){
            prs.updatePrmApprovedStatus(1,prmId);
        }else{
            //If rejected, the workflow is finished
            prs.updatePrmApprovedStatus(0,prmId);
            prs.updatePrmFinishedStatus(1,prmId);
        }
        //if reaches the end node, set the workflow finished
        if (role.equals(TASK_MANAGER_DIRECTOR_GROUP)){
            prs.updatePrmFinishedStatus(1,prmId);
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reEdit/{prmId}")
    public ResponseEntity<?> setRejectedPrToEditByPrmId(@PathVariable int prmId){
        boolean result = prProcessService.revokeProcessInstance(prmId);
        prs.updatePrmApprovedStatus(0,prmId);
        prs.updatePrmFinishedStatus(0,prmId);
        prs.updatePrmSubmittedStatus(0,prmId);
        int resultDelete = prs.deleteProcurementRequisitionCommentByPrmId(prmId);


        System.out.println("resultDelete : " + resultDelete);
        return ResponseEntity.noContent().build();
    }
}
