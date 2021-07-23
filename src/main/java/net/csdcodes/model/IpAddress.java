package net.csdcodes.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="ip_address")
public class IpAddress {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    protected int id;

    @Column(name="ip_address")
    protected String ipAddress;

    @Column(name="type")
    protected String type;

    @Column(name="device")
    protected String device;

    @Column(name="id_password")
    protected String idPassword;

    @Column(name="comment")
    protected String comment;

    @Column(name="created_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    protected Date createdDate;

    @Column(name="updated_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    protected Date updatedDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getIdPassword() {
        return idPassword;
    }

    public void setIdPassword(String idPassword) {
        this.idPassword = idPassword;
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

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "IpAddress{" +
                "id=" + id +
                ", ipAddress='" + ipAddress + '\'' +
                ", type='" + type + '\'' +
                ", device='" + device + '\'' +
                ", idPassword='" + idPassword + '\'' +
                ", comment='" + comment + '\'' +
                ", created_date=" + createdDate +
                ", updated_date=" + updatedDate +
                '}';
    }
}
