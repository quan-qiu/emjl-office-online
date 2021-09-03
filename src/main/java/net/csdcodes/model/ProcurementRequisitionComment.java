package net.csdcodes.model;

import liquibase.pro.packaged.C;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pr_comment")
public class ProcurementRequisitionComment {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "pr_comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int prCommentId;

    @Column(name = "pr_main_id")
    protected int prMainId;

    @Column(name = "userssn")
    protected String userSsn;

    @Column(name = "user_org_name")
    protected String userOrgName;

    @Column(name = "comment")
    protected String comment;

    @Column(name = "created_date")
    protected Date createdDate;

    @Column(name = "approved")
    protected int approved;

    @Column(name="gate")
    protected String gate;

    public int getPrCommentId() {
        return prCommentId;
    }

    public void setPrCommentId(int prCommentId) {
        this.prCommentId = prCommentId;
    }

    public int getPrMainId() {
        return prMainId;
    }

    public void setPrMainId(int prMainId) {
        this.prMainId = prMainId;
    }

    public String getUserSsn() {
        return userSsn;
    }

    public void setUserSsn(String userSsn) {
        this.userSsn = userSsn;
    }

    public String getUserOrgName() {
        return userOrgName;
    }

    public void setUserOrgName(String userOrgName) {
        this.userOrgName = userOrgName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    @Override
    public String toString() {
        return "ProcurementRequisitionComment{" +
                "prCommentId=" + prCommentId +
                ", prMainId=" + prMainId +
                ", userSsn='" + userSsn + '\'' +
                ", userOrgName='" + userOrgName + '\'' +
                ", comment='" + comment + '\'' +
                ", createdDate=" + createdDate +
                ", approved=" + approved +
                ", gate='" + gate + '\'' +
                '}';
    }
}
