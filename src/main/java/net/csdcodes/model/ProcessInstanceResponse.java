package net.csdcodes.model;

public class ProcessInstanceResponse {
    protected String processId;
    protected boolean isEnded;

    public ProcessInstanceResponse(String processId, boolean isEnded) {
        this.processId = processId;
        this.isEnded = isEnded;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public void setEnded(boolean ended) {
        isEnded = ended;
    }

    @Override
    public String toString() {
        return "ProcessInstanceResponse{" +
                "processId='" + processId + '\'' +
                ", isEnded=" + isEnded +
                '}';
    }
}
