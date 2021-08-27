package net.csdcodes.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import net.csdcodes.model.*;
import net.csdcodes.model.ls.PrFlowEnum;
import net.csdcodes.model.ls.PrUploadFileDirEnum;
import net.csdcodes.service.PrProcessService;
import net.csdcodes.service.ProcurementRequisitionService;
import net.csdcodes.service.StorageService;
import net.csdcodes.service.UserService;
import net.csdcodes.util.PrmExcelExporter;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pr")
public class ProcurementRequisitionController {

    Logger logger = LoggerFactory.getLogger(ProcurementRequisitionController.class);

    @Autowired
    UserService userService;
    @Autowired
    ProcurementRequisitionService prs;
    @Autowired
    StorageService storageService;
    @Autowired
    PrProcessService prProcessService;


    @GetMapping("")
    public String index(HttpServletRequest request, Model model){
        return "pr/index";
    }

    @GetMapping("/prm/saved/list")
    public String getSavedPR(HttpServletRequest request, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User thisUser = userService.getUserByUsername(auth.getName());

        List prSaved = prs.getPRMainSavedList(thisUser.getSsn());

        model.addAttribute("pr_saved", prSaved);

        return "pr/prm_save_list";
    }

    @GetMapping("/prm/flow_type")
    public String getFlowType(Model model){
        /*List flowType = new ArrayList();
        flowType.add(PrFlowEnum.RM_ASSEMBLY_MRO);
        flowType.add(PrFlowEnum.EXPENSE);
        flowType.add(PrFlowEnum.CAPITALIZED_PROJECT_FIXED_ASSET);*/

        List flowType = FlowType.getFlowType();

        List<Costcenter> ccs = prs.getCostcenterDesc();

        List pps = prs.getPrProject();

        model.addAttribute("flow_type",flowType);
        model.addAttribute("ccs",ccs);

        return "pr/flow_type";
    }


    @GetMapping("/prm/{id}")
    public String getPRMById(@PathVariable int id, Model model){

        ProcurementRequisitionMain prm = prs.getPRMainById(id);
        List prds = prs.getPRDetailsByPRMId(id);
        List ccs = prs.getCostcenterDesc();
        List cts = prs.getCostType(prm.getCostCenter());
        List pps = prs.getPrProject();

        model.addAttribute("prds", prds);
        model.addAttribute("prm", prm);
        model.addAttribute("ccs",ccs);
        model.addAttribute("cts",cts);
        model.addAttribute("pps", pps);

        String templateUrl = MvcUriComponentsBuilder
                .fromMethodName(FileUploadController.class, "serveFile" ,
                        PrUploadFileDirEnum.PR_PRD_TEMPLATE.name().toString(),
                        "prd_template.xlsx")
                .build()
                .toString();
        //System.out.println(templateUrl);
        model.addAttribute("templateUrl", templateUrl);

        return "pr/prm_edit";
    }

    @GetMapping("/prm/read/{prmId}/{assignee}")
    public String readPRMById(@PathVariable int prmId,@PathVariable String assignee,
                              Model model){
        Task task = prProcessService.getTaskByPrmId(prmId);
        if (null != task){

            String taskId = task.getId();

            Map<String, Object> variables= prProcessService.getTaskInstanceVariable(taskId);

            if(!assignee.equals(variables.get("curtAssignee"))){
                return "redirect:/pr/prm/history/" + prmId ;
            }

            model.addAttribute("taskId", taskId);
            model.addAttribute("curtAssignee", variables.get("curtAssignee"));
            model.addAttribute("nextAssignee", variables.get("nextAssignee"));
            model.addAttribute("managerSsn", variables.get("managerSsn"));
            model.addAttribute("orgName", variables.get("orgName"));


        }else{

            model.addAttribute("taskId", "");
            model.addAttribute("curtAssignee", "");
            model.addAttribute("nextAssignee", "");
            model.addAttribute("managerSsn", "");
            model.addAttribute("orgName", "");
        }
        ProcurementRequisitionMain prm = prs.getPRMainById(prmId);
        List prds = prs.getPRDetailsByPRMId(prmId);
        List prComments = prs.getProcurementRequisitionComment(prmId);

        model.addAttribute("prds", prds);
        model.addAttribute("prm", prm);
        model.addAttribute("prcs", prComments);
        model.addAttribute("userSsn", prm.getAplUserSsn());

        return "pr/prm_readonly";
    }

    @GetMapping("/prm/history")
    public String readPRMHistory(Model model){
        List flowType = FlowType.getFlowType();
        model.addAttribute("flow_type", flowType);

        return "pr/prm_history";
    }

    @PostMapping("/prm/history/query")
    public String queryPRMHistory(@RequestBody PRMRequestVariables prmrv, Model model)  {
        List<ProcurementRequisitionMain> prms = prs.getPRMHistory(prmrv);
        model.addAttribute("prms",prms);

        return "pr/prm_history_query_read";
    }

    @GetMapping("prm/userSubmitted")
    public String queryPRMSubmittedByUser( Model model)  {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User thisUser = userService.getUserByUsername(auth.getName());

        List<ProcurementRequisitionMain> prms = prs.PRMSubmittedByUser(thisUser.getSsn());
        model.addAttribute("prms",prms);

        return "pr/prm_history_query";
    }

    @GetMapping("/prm/history/{prmId}")
    public String viewPRMHistoryById(@PathVariable int prmId, Model model)  {

        ProcurementRequisitionMain prm = prs.getPRMainById(prmId);
        List<ProcurementRequisitionDetail> prds = prs.getPRDetailsByPRMId(prmId);
        List<ProcurementRequisitionComment> prcs = prs.getProcurementRequisitionComment(prmId);

        model.addAttribute("prm",prm);
        model.addAttribute("prds",prds);
        model.addAttribute("prcs", prcs);
        //System.out.println(prm.toString());
        return "pr/prm_history_view";
    }

    @GetMapping("/prds/{prmId}")
    public String getPRDetailsByPRMId(@PathVariable int prmId, Model model){


        List prds = prs.getPRDetailsByPRMId(prmId);

        model.addAttribute("prds", prds);

        return "pr/prd_list";
    }

    @GetMapping("/prd/{prdId}/{erpCode}")
    public String getPRDetailsByPRDId(@PathVariable int prdId,@PathVariable String erpCode, Model model){

        ProcurementRequisitionDetail prd = prs.getPRDetailsByPRDId(prdId);

        model.addAttribute("prd", prd);
        model.addAttribute("ec", erpCode);

        return "pr/prd_edit";
    }

    @GetMapping("/prd/read/{prdId}")
    public String readPRDetailsByPRDId(@PathVariable int prdId, Model model){

        ProcurementRequisitionDetail prd = prs.getPRDetailsByPRDId(prdId);

        model.addAttribute("prd", prd);

        return "pr/prd_readonly";
    }

    @GetMapping("/prd/new/{prmId}/{erpCode}")
    public String getNewPRDetail(@PathVariable int prmId, @PathVariable String erpCode,Model model){

        ProcurementRequisitionDetail prd = new ProcurementRequisitionDetail();
        prd.setPrMainId(prmId);
        model.addAttribute("prd", prd);
        model.addAttribute("ec", erpCode);
        return "pr/prd_new";
    }

    @GetMapping("/prd/upload/{prmId}")
    public String uploadFileForm(@PathVariable int prmId, Model model) throws IOException {
        model.addAttribute("prmId", prmId);
        return "pr/upload_form";
    }

    @GetMapping("/confirmPR")
    public String getConfirmPRForm(Model model){

        List<User> roleList = prs.getUserByRole("PR-MANAGER");
        model.addAttribute("roleList", roleList);
        return "pr/confirmPR_form";
    }

    @GetMapping("/mytasks")
    public String getMyTasks(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User thisUser = userService.getUserByUsername(auth.getName());

        Set<Role> roles = userService.getUserRoles(thisUser.getUserId());

        Iterator<Role> itr = roles.iterator();
        Set<TaskDetails> tasks = new HashSet<>();
        while (itr.hasNext()){
            List<TaskDetails> taskDetails = prProcessService.getTasksByGroup(itr.next().getName());
            Iterator<TaskDetails> itt = taskDetails.iterator();
            while (itt.hasNext()){
                tasks.add(itt.next());
            }
        }

        List<TaskDetails> personalTasks = prProcessService.getTasksByAssignee(thisUser.getSsn());
        tasks.addAll(personalTasks);

        model.addAttribute("myTasks",tasks);

        return "pr/myTasks";
    }

    @GetMapping("/prm/download/{completed}/{agreed}/{start}/{end}/{flowType}/{poCode}")
    public void exportToExcel(

            @PathVariable int completed,
            @PathVariable int agreed,
                              @PathVariable String start,
                              @PathVariable String end,
                              @PathVariable String flowType,
                              @PathVariable String poCode,
                              HttpServletResponse response) throws IOException, ParseException {

        response.setContentType("application/octet-stream");
       // response.setContentType("text/plain");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=prms_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        if (start.equals("null")) { start = null ;};
        if (end.equals("null")) { end = null ;};
        if (poCode.equals("null")) { poCode = null ;};
        if (flowType.equals("null")) { flowType = null ;};

        PRMRequestVariables prmrv = new PRMRequestVariables(completed,agreed,1,start,end,poCode,flowType);

        List<ProcurementRequisitionMain> prms = prs.getPRMHistory(prmrv);
        Iterator<ProcurementRequisitionMain> ittPrm = prms.iterator();
        List<PrMainDetail> prmds = new ArrayList<>();

        while (ittPrm.hasNext()){
            ProcurementRequisitionMain prm = ittPrm.next();
            List<ProcurementRequisitionDetail> prds = prs.getPRDetailsByPRMId(prm.getId());
            Iterator<ProcurementRequisitionDetail> ittPrd = prds.iterator();
            while (ittPrd.hasNext()){
                PrMainDetail prMainDetail = new PrMainDetail();
                ProcurementRequisitionDetail prd = ittPrd.next();
                prMainDetail.setPrm(prm);
                prMainDetail.setPrd(prd);
                prmds.add(prMainDetail);
            }
        }

        PrmExcelExporter excelExporter = new PrmExcelExporter(prmds);
        excelExporter.export(response);

    }


}
