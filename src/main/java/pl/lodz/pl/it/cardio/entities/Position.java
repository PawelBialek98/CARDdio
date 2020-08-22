package pl.lodz.pl.it.cardio.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
