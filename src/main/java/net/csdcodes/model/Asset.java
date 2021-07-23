package net.csdcodes.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name="asset")
public class Asset {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    protected int id;
    @Column(name="asset_id")
    protected String assetId;
    @Column(name="user_name")
    protected String userName;
    @Column(name="ssn")
    protected String ssn;
    @Column(name="dept")
    protected String dept;
    @Column(name="asset_type")
    protected String assetType;
    @Column(name="type")
    protected String type;
    @Column(name="brand_model")
    protected String brandModel;
    @Column(name="sn")
    protected String sn;
    @Column(name="location")
    protected String location;
    @Column(name="purchase_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    protected Date purchaseDate;
    @Column(name="comment")
    protected String comment;
    @Column(name="status")
    protected String status;

    public Asset(){}

    public Asset(String assetId,String userName, String dept, String assetType,
                 String type, String brandModel,String sn,String location,
                 Date purchaseDate, String comment, String status){
        this.assetId = assetId;
        this.userName = userName;
        this.dept = dept;
        this.assetType = assetType;
        this.type = type;
        this.brandModel = brandModel;
        this.sn = sn;
        this.brandModel = brandModel;
        this.location = location;
        this.purchaseDate = purchaseDate;
        this.comment = comment;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "id=" + id +
                ", assetId='" + assetId + '\'' +
                ", ssn='" + ssn + '\'' +
                ", userName='" + userName + '\'' +
                ", dept='" + dept + '\'' +
                ", assetType='" + assetType + '\'' +
                ", type='" + type + '\'' +
                ", brandModel='" + brandModel + '\'' +
                ", sn='" + sn + '\'' +
                ", location='" + location + '\'' +
                ", purchaseDate=" + purchaseDate +
                ", comment='" + comment + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
