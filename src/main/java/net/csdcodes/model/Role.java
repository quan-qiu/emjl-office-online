package net.csdcodes.model;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int roleId;

    @Column(name = "name")
    protected String name;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", name='" + name + '\'' +
                '}';
    }
}
