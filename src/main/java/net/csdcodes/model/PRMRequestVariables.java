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

    public PRMRequestVariables(int approved, int finished, int submitted, String prmStart, String prmEnd) throws ParseException {
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

    @Override
    public String toString() {
        return "PRMRequestVariables{" +
                "approved=" + approved +
                ", finished=" + finished +
                ", submitted=" + submitted +
                ", prmStart=" + prmStart +
                ", prmEnd=" + prmEnd +
                '}';
    }
}
