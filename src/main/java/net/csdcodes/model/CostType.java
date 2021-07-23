package net.csdcodes.model;

import javax.persistence.*;

@Entity
@Table(name="cost_type")
public class CostType {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    protected int id;

    @Column(name="costcenter")
    protected String costcenter;

    @Column(name="cost_type")
    protected String costType;

    @Column(name="erp_code")
    protected String erpCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCostcenter() {
        return costcenter;
    }

    public void setCostcenter(String costcenter) {
        this.costcenter = costcenter;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    @Override
    public String toString() {
        return "CostType{" +
                "id=" + id +
                ", costcenter='" + costcenter + '\'' +
                ", costType='" + costType + '\'' +
                ", erpCode='" + erpCode + '\'' +
                '}';
    }
}
