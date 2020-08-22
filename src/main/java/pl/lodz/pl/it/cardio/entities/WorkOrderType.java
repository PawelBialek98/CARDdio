package pl.lodz.pl.it.cardio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Period;

@Entity
@Table(name = "work_order_type_t")
public class WorkOrderType extends BaseEntity{

    @Column
    @Getter
    @Setter
    private String name;

    @Column
    @Getter
    @Setter
    private String code;

    @Column(name = "required_time")
    @Getter
    @Setter
    private int requiredTime;

    @JoinColumn(name = "skill_id", referencedColumnName = "id")
    @ManyToOne
    private Skill requiredSkill;

}
