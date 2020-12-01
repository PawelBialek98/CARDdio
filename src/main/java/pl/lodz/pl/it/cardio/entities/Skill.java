package pl.lodz.pl.it.cardio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

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
