package net.csdcodes.service;

import com.google.gson.JsonObject;
import net.csdcodes.model.ProcessInstanceResponse;
import net.csdcodes.model.ProcurementRequisitionMain;

import net.csdcodes.model.TaskDetails;
import net.csdcodes.model.User;
import org.flowable.cmmn.api.CmmnHistoryService;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PrProcessService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    //public static final String TASK_CANDIDATE_GROUP = "managers";
    public static final String TASK_AUDITOR_GROUP = "PR-AUDITOR";
    public static final String TASK_MANAGER_GROUP = "PR-MANAGER";
    public static final String TASK_DEPUTY_MANAGER_GROUP = "PR-DEPUTY-MANAGING-DIRECTOR";
    public static final String TASK_MANAGER_DIRECTOR_GROUP = "PR-MANAGING-DIRECTOR";
    public static final String TASK_PURCHASER_GROUP = "PR-PURCHASER";
    public static final String TASK_FINISHED = "FINISHED";
    public static final String PROCESS_DEFINITION_KEY = "PR-Request";
    public static final String EMP_NAME = "empName";

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;

    ProcessEngine processEngine;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    ProcurementRequisitionService prs;

    public void deployProcessDefinition(){
        System.out.println("---------------------------");
        Deployment deployment = repositoryService
                .createDeployment()
                .addClasspathResource("processes/pr-approval-flow.bpmn20.xml")
                .deploy();
        System.out.println("---------------------------");
    }

    public ProcessInstanceResponse initiateWorkflow(JsonObject variablesObj, User thisUser)
    //public void initiateWorkflow(JsonObject variablesObj, User thisUser)
        throws FlowableException, FlowableObjectNotFoundException{
        //System.out.println("initiating new process in Flowable workflow for PR for user " + thisUser.getOrgName());

        Map<String, Object> variables = new HashMap<>();
        variables.put("mainId", variablesObj.get("mainId").getAsString());
        variables.put("dept", variablesObj.get("dept").getAsString());
        variables.put("orgName",thisUser.getOrgName());
        variables.put("userSsn",thisUser.getSsn());
        variables.put("prTitle",variablesObj.get("prTitle").getAsString());
        variables.put("status","Pending for an auditor to approve");
        variables.put("curtAssignee",TASK_AUDITOR_GROUP);
        variables.put("nextAssignee",TASK_MANAGER_GROUP);
        variables.put("approved","Pending");
        variables.put("managerSsn",variablesObj.get("managerSsn").getAsString());

        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);
        //System.out.println("ProcessInstance id : " + processInstance.getId());
       // prs.updatePrmSubmittedStatus(variablesObj.get("mainId").getAsInt(),1);

        return new ProcessInstanceResponse(processInstance.getId(), processInstance.isEnded());

    }

    public List<TaskDetails> getTasksByGroup(String groupName) {
        List<Task> tasks =
                taskService.createTaskQuery().taskCandidateGroup(groupName).list();
        List<TaskDetails> taskDetails = getTaskDetails(tasks);

        return taskDetails;
    }

    public Task getTaskByPrmId(int prmId){
        Task task = taskService.createTaskQuery().processVariableValueEquals("mainId", String.valueOf(prmId)).singleResult();

        if (task != null){
            return task;
        }else{
            return null;
        }

    }

    public ProcessInstance getProcessInstanceByPrmId(String prmId){

        ProcessInstance processInstance = processEngine.getRuntimeService().createProcessInstanceQuery().variableValueEquals("mainId",prmId).singleResult();
        if (processInstance != null){
            return processInstance;
        }else{
            return null;
        }

    }
/*
    public List<TaskDetails> getManagerTasks() {
        List<Task> tasks =
                taskService.createTaskQuery().taskCandidateGroup(TASK_MANAGER_GROUP).list();
        List<TaskDetails> taskDetails = getTaskDetails(tasks);

        return taskDetails;
    }*/

    public List<TaskDetails> getTasksByAssignee(String assignee){

        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
        List<TaskDetails> taskDetails = getTaskDetails(tasks);

        return taskDetails;
    }

    public List<TaskDetails> getUserTasks(){
        List<Task> tasks = taskService.createTaskQuery().taskUnassigned().list();
        //List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
        List<TaskDetails> taskDetails = getTaskDetails(tasks);

        return taskDetails;
    }

    public void approvePr(String role, String taskId,int approved) {

        //System.out.println("===========start==============");
        //System.out.println("taskid: " + taskId);
        //System.out.println("role: " + role);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processId= task.getProcessInstanceId();
        Map<String, Object> variables = runtimeService.getVariables(processId);

        Map<String, Object> approveVar = new HashMap<String, Object>();

        if(role.equals(TASK_AUDITOR_GROUP)) {
            //System.out.println("----------- update assignee : TASK_AUDITOR_GROUP");
            runtimeService.setVariable(processId, "curtAssignee",TASK_MANAGER_GROUP);
            runtimeService.setVariable(processId, "nextAssignee",TASK_DEPUTY_MANAGER_GROUP);
        }
        if(!role.startsWith("PR-")){
            runtimeService.setVariable(processId, "curtAssignee",TASK_DEPUTY_MANAGER_GROUP);
            runtimeService.setVariable(processId, "nextAssignee",TASK_MANAGER_DIRECTOR_GROUP);
        }
        if(role.equals(TASK_DEPUTY_MANAGER_GROUP)){
            runtimeService.setVariable(processId, "curtAssignee",TASK_MANAGER_DIRECTOR_GROUP);
            runtimeService.setVariable(processId, "nextAssignee",TASK_PURCHASER_GROUP);
        }
        if(role.equals(TASK_MANAGER_DIRECTOR_GROUP)){
            runtimeService.setVariable(processId, "curtAssignee",TASK_PURCHASER_GROUP);
            runtimeService.setVariable(processId, "nextAssignee",TASK_FINISHED);
        }
        if(role.equals(TASK_PURCHASER_GROUP)){
          runtimeService.setVariable(processId, "curtAssignee",TASK_FINISHED);
          runtimeService.setVariable(processId, "nextAssignee",TASK_FINISHED);
        }
        //System.out.println("curtAssignee: " + variables.get("curtAssignee"));
        //System.out.println("nextAssignee: " + variables.get("nextAssignee"));
        runtimeService.setVariable(processId,"approved", approved);
        runtimeService.setVariable(processId,"approvedBy", role);

        if (approved == 1){
            approveVar.put("approved", true);
        }else{
            approveVar.put("approved", false);
        }

        //System.out.println("approveVar: " + approveVar);
        //System.out.println("===========end==============");
        taskService.complete(taskId, approveVar);

    }

    private List<TaskDetails> getTaskDetails(List<Task> tasks){
        List<TaskDetails> taskDetails = new ArrayList<>();
        for (Task task : tasks){
            Map<String , Object> processVariables = taskService.getVariables(task.getId());
            taskDetails.add(new TaskDetails(task.getId(),task.getName(),processVariables, task.getTaskDefinitionId()));
        }
        return taskDetails;
    }

    public void checkProcessHistory(String processId) {

        HistoryService historyService = processEngine.getHistoryService();

        List<HistoricActivityInstance> activities =
                historyService
                        .createHistoricActivityInstanceQuery()
                        .processInstanceId(processId)
                        .finished()
                        .orderByHistoricActivityInstanceEndTime()
                        .asc()
                        .list();

        for (HistoricActivityInstance activity : activities) {
            System.out.println(
                    activity.getActivityId() + " took " + activity.getDurationInMillis() + " milliseconds");
        }

        System.out.println("\n \n \n \n");
    }


    public Map<String, Object> getTaskInstanceVariable(String taskId){
        System.out.println("taskid:" + taskId);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        System.out.println(task.toString());
        String processId= task.getProcessInstanceId();
        Map<String, Object> variables = runtimeService.getVariables(processId);

        return variables;
    }

    public boolean revokeProcessInstance(int prmId){
        try {
            Task task = getTaskByPrmId(prmId);

            if (Objects.isNull(task)) {
                System.out.println("task is null");
                return false;
            }
            System.out.println(task.toString());
            String processInstanceId = task.getProcessInstanceId();
            System.out.println("processInstanceId: " + processInstanceId);
            if (processInstanceId == null) {
                System.out.println("processInstanceId is null");
                return false;
            }
            runtimeService.deleteProcessInstance(processInstanceId, "User revoke");

            return true;

        }catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }
        return false;
    }


}
