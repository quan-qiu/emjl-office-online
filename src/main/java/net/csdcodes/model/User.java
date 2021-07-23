package net.csdcodes.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="user_id")
    protected int userId;

    @Column(name="orgname")
    protected String orgName;

    @Column(name="username")
    protected String userName;

    @Column(name="ssn")
    protected String ssn;

    @Column(name="dept")
    protected String dept;

    @Column(name="password")
    protected String password;

    @Column(name="email")
    protected String email;

    @Column(name="enable")
    protected int enable;

    @Column(name="created_date")
    protected Date createdDate;

    @Column(name="updated_date")
    protected Date updatedDate;

    /*@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )*/
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    private String sRoles;

    public User() {
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
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

    public String getsRoles() {
        return sRoles;
    }

    public void setsRoles(String sRoles) {
        this.sRoles = sRoles;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", orgName='" + orgName + '\'' +
                ", userName='" + userName + '\'' +
                ", ssn='" + ssn + '\'' +
                ", dept='" + dept + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", enable=" + enable +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", roles=" + roles +
                ", sRoles='" + sRoles + '\'' +
                '}';
    }
}
