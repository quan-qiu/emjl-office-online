package net.csdcodes.model;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="pr_main")
public class ProcurementRequisitionMain {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    protected int id;
    @Column(name="pr_title")
    protected String prTitle;
    @Column(name="apl_user_ssn")
    protected String aplUserSsn;
    @Column(name="apl_user_name")
    protected String aplUserName;
    @Column(name="cost_center")
    protected String costCenter;
    @Column(name="apl_dept")
    protected String aplDept;
    @Column(name="pr_no")
    protected String prNo;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Column(name="pr_apl_date")
    protected Date prAplDate;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Column(name="pr_apl_update_date")
    protected Date prAplUpdateDate;
    @Column(name="project_name")
    protected String projectName;
    @Column(name="pr_status")
    protected String prStatus;
    @Column(name="pr_process")
    protected String prProcess;
    @Column(name="pr_pass")
    protected String prPass;
    @Column(name="flow_type")
    protected String flowType;
    @Column(name="finished")
    protected int finished;
    @Column(name="approved")
    protected int approved;
    @Column(name="submitted")
    protected int submitted;
    @Column(name = "po_code")
    protected String poCode;

    protected float totalEstCost;


    public ProcurementRequisitionMain() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrTitle() {
        return prTitle;
    }

    public void setPrTitle(String prTitle) {
        this.prTitle = prTitle;
    }

    public String getAplUserSsn() {
        return aplUserSsn;
    }

    public void setAplUserSsn(String aplUserSsn) {
        this.aplUserSsn = aplUserSsn;
    }

    public String getAplUserName() {
        return aplUserName;
    }

    public void setAplUserName(String aplUserName) {
        this.aplUserName = aplUserName;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getAplDept() {
        return aplDept;
    }

    public void setAplDept(String aplDept) {
        this.aplDept = aplDept;
    }

    public String getPrNo() {
        return prNo;
    }

    public void setPrNo(String prNo) {
        this.prNo = prNo;
    }

    public Date getPrAplDate() {
        return prAplDate;
    }

    public void setPrAplDate(Date prAplDate) {
        this.prAplDate = prAplDate;
    }

    public Date getPrAplUpdateDate() {
        return prAplUpdateDate;
    }

    public void setPrAplUpdateDate(Date prAplUpdateDate) {
        this.prAplUpdateDate = prAplUpdateDate;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPrStatus() {
        return prStatus;
    }

    public void setPrStatus(String prStatus) {
        this.prStatus = prStatus;
    }

    public String getPrProcess() {
        return prProcess;
    }

    public void setPrProcess(String prProcess) {
        this.prProcess = prProcess;
    }

    public String getPrPass() {
        return prPass;
    }

    public void setPrPass(String prPass) {
        this.prPass = prPass;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getSubmitted() {
        return submitted;
    }

    public void setSubmitted(int submitted) {
        this.submitted = submitted;
    }

    public float getTotalEstCost() {
        return totalEstCost;
    }

    public void setTotalEstCost(float totalEstCost) {
        this.totalEstCost = totalEstCost;
    }

    public String getPoCode() {
        return poCode;
    }

    public void setPoCode(String poCode) {
        this.poCode = poCode;
    }

    @Override
    public String toString() {
        return "ProcurementRequisitionMain{" +
                "id=" + id +
                ", prTitle='" + prTitle + '\'' +
                ", aplUserSsn='" + aplUserSsn + '\'' +
                ", aplUserName='" + aplUserName + '\'' +
                ", costCenter='" + costCenter + '\'' +
                ", aplDept='" + aplDept + '\'' +
                ", prNo='" + prNo + '\'' +
                ", prAplDate=" + prAplDate +
                ", prAplUpdateDate=" + prAplUpdateDate +
                ", projectName='" + projectName + '\'' +
                ", prStatus='" + prStatus + '\'' +
                ", prProcess='" + prProcess + '\'' +
                ", prPass='" + prPass + '\'' +
                ", flowType='" + flowType + '\'' +
                ", finished=" + finished +
                ", approved=" + approved +
                ", submitted=" + submitted +
                ", poCode='" + poCode + '\'' +
                ", totalEstCost=" + totalEstCost +
                '}';
    }
}
