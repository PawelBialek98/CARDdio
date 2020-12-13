package pl.lodz.p.it.cardio.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


//TODO do usunięcia
@Entity
@Table(name = "position_t")
public class Position extends BaseEntity {

    @Column
    private String name;

    @Override
    public String toString() {
        return "Position{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}
