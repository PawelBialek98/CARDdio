package pl.lodz.pl.it.cardio.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "employee_t")
//@PrimaryKeyJoinColumn(name = "user_id")
//public class Employee extends User {
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private int id;

    @Column(name = "birth_date")
    @Getter
    @Setter
    private Date birth;

    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false, nullable = false)
    @OneToOne(optional = false)
    @NotNull
    @Getter
    private User user;

    @JsonIgnore
    @ManyToMany(mappedBy = "employees")
    private  Collection<WorkOrderType> skills;

    @Override
    public String toString() {
        return "Employee{" +
                "birth=" + birth +
                "} " + super.toString();
    }
}
