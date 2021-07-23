package net.csdcodes.model;

import javax.persistence.*;

@Entity
@Table(name="costcenter")
public class Costcenter {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    protected int id;

    @Column(name="costcenter")
    protected String costcenter;

    @Column(name="description")
    protected String description;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "Costcenter{" +
                "id=" + id +
                ", costcenter='" + costcenter + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
