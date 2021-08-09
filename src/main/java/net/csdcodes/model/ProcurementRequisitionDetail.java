package net.csdcodes.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="pr_detail")
public class ProcurementRequisitionDetail {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    protected int id;
    @Column(name="pr_main_id")
    protected int prMainId;
    @Column(name="item_erp_code")
    protected String itemErpCode;
    @Column(name="item_erp_desc")
    protected String itemErpDesc;
    @Column(name="item_erp_brand_size")
    protected String itemErpBrandSize;
    @Column(name="qty")
    protected float qty;
    @Column(name="item_erp_unit")
    protected String itemErpUnit;
    @Column(name="est_cost")
    protected float estCost;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Column(name="created_date")
    protected Date createdDate;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Column(name="updated_date")
    protected Date updatedDate;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Column(name="target_date")
    protected Date targetDate;

    protected float totalEstCost;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrMainId() {
        return prMainId;
    }

    public void setPrMainId(int prMainId) {
        this.prMainId = prMainId;
    }

    public String getItemErpCode() {
        return itemErpCode;
    }

    public void setItemErpCode(String itemErpCode) {
        this.itemErpCode = itemErpCode;
    }

    public String getItemErpDesc() {
        return itemErpDesc;
    }

    public void setItemErpDesc(String itemErpDesc) {
        this.itemErpDesc = itemErpDesc;
    }

    public String getItemErpBrandSize() {
        return itemErpBrandSize;
    }

    public void setItemErpBrandSize(String itemErpBrandSize) {
        this.itemErpBrandSize = itemErpBrandSize;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public String getItemErpUnit() {
        return itemErpUnit;
    }

    public void setItemErpUnit(String itemErpUnit) {
        this.itemErpUnit = itemErpUnit;
    }

    public float getEstCost() {
        return estCost;
    }

    public void setEstCost(float estCost) {
        this.estCost = estCost;
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

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public float getTotalEstCost() {
        return totalEstCost;
    }

    public void setTotalEstCost(float totalEstCost) {
        this.totalEstCost = totalEstCost;
    }

    @Override
    public String toString() {
        return "ProcurementRequisitionDetail{" +
                "id=" + id +
                ", prMainId=" + prMainId +
                ", itemErpCode='" + itemErpCode + '\'' +
                ", itemErpDesc='" + itemErpDesc + '\'' +
                ", itemErpBrandSize='" + itemErpBrandSize + '\'' +
                ", qty=" + qty +
                ", itemErpUnit='" + itemErpUnit + '\'' +
                ", estCost=" + estCost +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", targetDate=" + targetDate +
                ", totalEstCost=" + totalEstCost +
                '}';
    }
}
