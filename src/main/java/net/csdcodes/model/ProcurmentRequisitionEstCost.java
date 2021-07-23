package net.csdcodes.model;

public class ProcurmentRequisitionEstCost {
    protected int prmId;

    protected float estCost;

    public ProcurmentRequisitionEstCost(int prmId, float estCost) {
        this.prmId = prmId;
        this.estCost = estCost;
    }

    public int getPrmId() {
        return prmId;
    }

    public void setPrmId(int prmId) {
        this.prmId = prmId;
    }

    public float getEstCost() {
        return estCost;
    }

    public void setEstCost(float estCost) {
        this.estCost = estCost;
    }
}
