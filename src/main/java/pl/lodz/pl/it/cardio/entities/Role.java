package pl.lodz.pl.it.cardio.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collection;

@Entity
@Table(name = "role_t")
public class Role extends BaseEntity {

    @Column
    @Getter
    @Setter
    private String code;

    @Column
    @Getter
    @Setter
    private String name;

    @Column
    private Boolean isEnabled;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    @Override
    public String toString() {
        return "Role{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", is_enabled=" + isEnabled +
                "} " + super.toString();
    }
}
