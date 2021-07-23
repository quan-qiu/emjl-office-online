package net.csdcodes.model;

import java.util.Map;

public class TaskDetails {
    protected String taskId;
    protected String taskName;
    protected Map<String, Object> taskData;
    protected String taskDefinitionId;

    public TaskDetails(String taskId, String taskName, Map<String, Object> taskData,String taskDefinitionId) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskData = taskData;
        this.taskDefinitionId = taskDefinitionId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Map<String, Object> getTaskData() {
        return taskData;
    }

    public void setTaskData(Map<String, Object> taskData) {
        this.taskData = taskData;
    }

    public String getTaskDefinitionId() {
        return taskDefinitionId;
    }

    public void setTaskDefinitionId(String taskDefinitionId) {
        this.taskDefinitionId = taskDefinitionId;
    }

    @Override
    public String toString() {
        return "TaskDetails{" +
                "taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskData=" + taskData +
                ", taskDefinitionId='" + taskDefinitionId + '\'' +
                '}';
    }
}
