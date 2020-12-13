package pl.lodz.p.it.cardio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

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

    /*@JoinColumn(name = "skill_id", referencedColumnName = "id")
    @ManyToOne
    private Skill requiredSkill;*/

    @ManyToMany(mappedBy = "workOrderTypes")
    /*@JoinTable(
            name = "employee_skill_t",
            joinColumns = @JoinColumn(
                    name = "work_order_type_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "employee_id", referencedColumnName = "id"))*/
    private Collection<Employee> employees;

}
