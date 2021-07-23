package net.csdcodes.model;

import javax.persistence.*;

@Entity
@Table(name="hardware_status")
public class HardwareStatus {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    protected int id;
    @Column(name="name")
    protected String name;
    @Column(name="seq_id")
    protected int seqId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    @Override
    public String toString() {
        return "HardwareStatus{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", seqId='" + seqId + '\'' +
                '}';
    }
}
