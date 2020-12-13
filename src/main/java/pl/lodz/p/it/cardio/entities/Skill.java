package pl.lodz.p.it.cardio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

//TODO do usunięcia!
@Entity
@Table(name = "skill_t")
public class Skill extends BaseEntity{

    @Column
    @Getter
    @Setter
    private String name;

    @Column
    @Getter
    @Setter
    private String code;

}
