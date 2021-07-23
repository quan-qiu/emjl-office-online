package net.csdcodes.model;

import javax.persistence.*;

@Entity
@Table(name="hardware_type")
public class HardwareType {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    protected int id;
    @Column(name="type")
    protected String type;
    @Column(name="seq_id")
    protected int seqId;

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    @Override
    public String toString() {
        return "HardwareType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", seq_id=" + seqId +
                '}';
    }
}
