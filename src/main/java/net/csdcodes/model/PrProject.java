package net.csdcodes.model;

import javax.persistence.*;

@Entity
@Table(name="pr_project")
public class PrProject {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    protected int id;

    @Column(name="project_name")
    protected String projectName;

    @Column(name="erp_code")
    protected String erpCode;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    @Override
    public String toString() {
        return "PrProject{" +
                "id=" + id +
                ", projectName='" + projectName + '\'' +
                ", erpCode='" + erpCode + '\'' +
                '}';
    }
}
