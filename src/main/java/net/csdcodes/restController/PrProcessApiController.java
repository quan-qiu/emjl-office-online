package net.csdcodes.restController;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.csdcodes.model.ProcessInstanceResponse;
import net.csdcodes.model.ProcurementRequisitionMain;
import net.csdcodes.model.TaskDetails;
import net.csdcodes.model.User;
import net.csdcodes.service.PrProcessService;
import net.csdcodes.service.ProcurementRequisitionService;
import net.csdcodes.service.UserService;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

@RestController
@RequestMapping("/api/pr/process")
public class PrProcessApiController {
    Logger logger = LoggerFactory.getLogger(PrProcessApiController.class);

    @Autowired
    PrProcessService prProcessService;
    @Autowired
    UserService userService;
    @Autowired
    ProcurementRequisitionService prs;


    @PostMapping("/deploy")
    public void deployworkFlow(){
        prProcessService.deployProcessDefinition();
    }

    @PostMapping("/createPrRequest")
    public ProcessInstanceResponse createPrProcessInstance(@RequestBody String variables)
        throws FlowableException, FlowableObjectNotFoundException{

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User thisUser = userService.getUserByUsername(auth.getName());

        JsonElement variablesJE = new JsonParser().parse(variables);
        JsonObject variablesObject = variablesJE.getAsJsonObject();

        prs.updatePrmSubmittedStatus(1,variablesObject.get("mainId").getAsInt());
        prs.updatePrmApprovedStatus(0,variablesObject.get("mainId").getAsInt());
        prs.updatePrmFinishedStatus(0,variablesObject.get("mainId").getAsInt());

        return prProcessService.initiateWorkflow(variablesObject, thisUser);

    }

    /*@PostMapping("/completeTask")
    public void completeTask(@RequestBody TaskInfo taskInfo)
        throws FlowableException,FlowableObjectNotFoundException{
        try{
            if(null == taskInfo.getTaskDefinitionId()){
                System.out.println("TaskId can't be null to complete a userTask");
            }
            prProcessService.completeUseerTask(taskInfo);
        }
        catch (FlowableObjectNotFoundException flw){
            System.out.println("TaskId doesn't exists");
        }
    }

    @PostMapping("/claimTask")
    public void claimTask(@RequestBody TaskInfo taskInfo)
        throws FlowableException, FlowableObjectNotFoundException{
        try{
            if (null == taskInfo.getTaskDefinitionId()){
                System.out.println("Task can't be null to claim a userTasks");
            }
            prProcessService.claimTask(taskInfo);
        }catch (FlowableObjectNotFoundException flw){
            System.out.println("TaskId doesn't exists");
        }
    }

    @PostMapping("/unclaimTask")
    public void unclaimTask(@RequestBody TaskInfo taskInfo)
            throws FlowableException, FlowableObjectNotFoundException{
        try{
            if (null == taskInfo.getTaskDefinitionId()){
                System.out.println("Task can't be null to claim a userTasks");
            }
            prProcessService.unclaimTask(taskInfo);
        }catch (FlowableObjectNotFoundException flw){
            System.out.println("TaskId doesn't exists");
        }

    }*/

    @GetMapping("/getPendingTasks")
    public List<TaskDetails> getUserTasks(){
        return null;
        //return prProcessService.getUserTasks();
    }

    @GetMapping("/assignee/{assigneeSsn}/tasks")
    public List<TaskDetails> getTasksByAssignee(@PathVariable String assigneeSsn) {
        return prProcessService.getTasksByAssignee(assigneeSsn);
    }

    @PostMapping("/assignee/{assigneeSsn}/approve/tasks/{taskId}/{approved}")
    public void approveTaskByAssignee(@PathVariable String assigneeSsn,
                                      @PathVariable("taskId") String taskId,
                                      @PathVariable("approved") int approved){
        prProcessService.approvePr(assigneeSsn, taskId, approved);
    }

    @GetMapping("/role/{role}/tasks")
    public List<TaskDetails> getTasks(@PathVariable String role) {
        return prProcessService.getTasksByGroup(role);
    }

    @PostMapping("/role/{role}/approve/tasks/{taskId}/{approved}")
    public void approveTask(@PathVariable String role,
                            @PathVariable("taskId") String taskId,
                            @PathVariable("approved") int approved){
        prProcessService.approvePr(role, taskId, approved);

    }

    @GetMapping("/process/{processId}")
    public void checkState(@PathVariable("processId") String processId){
        prProcessService.checkProcessHistory(processId);
    }

    @PostMapping("/proIns/revoke/{prmId}")
    public String revokeProcessInstance(@PathVariable int prmId){
        boolean result = false;

        result = prProcessService.revokeProcessInstance(prmId);
        System.out.println("prProcessService.revokeProcessInstance(prmId):" + result);

        prs.updatePrmApprovedStatus(0,prmId);
        prs.updatePrmFinishedStatus(0,prmId);
        prs.updatePrmSubmittedStatus(0,prmId);
        int action = prs.deleteProcurementRequisitionCommentByPrmId(prmId);
        System.out.println("prs.deleteProcurementRequisitionCommentByPrmId(prmId):" + action);


        return "{\"finished\":"+ true + "}";
    }

    @GetMapping("/taskname/{prmId}")
    public ResponseEntity<?> getTaskname(@PathVariable int prmId){
        Task task = prProcessService.getTaskByPrmId(prmId);

        if (task != null)
            return new ResponseEntity<>(task.getName(),HttpStatus.OK);
        else
        return new ResponseEntity<>("Couldn't find",HttpStatus.OK);
    }
}
