package net.csdcodes.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PRMRequestVariables {

    private int approved;
    private int finished;
    private int submitted;
    private Date prmStart;
    private Date prmEnd;
    private String poCode;
    private String flowType;

    public PRMRequestVariables(int approved, int finished, int submitted, String prmStart, String prmEnd,String poCode, String flowType) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        this.approved = approved;
        this.finished = finished;
        this.submitted = submitted;
        if (!(null == prmStart) && !("".equals(prmStart))){
            this.prmStart = formatter.parse(prmStart);
        }
        if (!(null == prmEnd) && !("".equals(prmEnd))) {
            this.prmEnd = formatter.parse(prmEnd);
        }
        if (!(null == poCode) && !("".equals(poCode))) {
            this.poCode = poCode;
        }else {
            this.poCode = null;
        }
        if (!(null == flowType) && !("".equals(flowType))) {
            this.flowType = flowType;
        }else{
            this.flowType = null;
        }
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getSubmitted() {
        return submitted;
    }

    public void setSubmitted(int submitted) {
        this.submitted = submitted;
    }

    public Date getPrmStart() {
        return prmStart;
    }

    public void setPrmStart(Date prmStart) {
        this.prmStart = prmStart;
    }

    public Date getPrmEnd() {
        return prmEnd;
    }

    public void setPrmEnd(Date prmEnd) {
        this.prmEnd = prmEnd;
    }

    public String getPoCode() {
        return poCode;
    }

    public void setPoCode(String poCode) {
        this.poCode = poCode;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    @Override
    public String toString() {
        return "PRMRequestVariables{" +
                "approved=" + approved +
                ", finished=" + finished +
                ", submitted=" + submitted +
                ", prmStart=" + prmStart +
                ", prmEnd=" + prmEnd +
                ", poCode='" + poCode + '\'' +
                ", flowType='" + flowType + '\'' +
                '}';
    }
}
