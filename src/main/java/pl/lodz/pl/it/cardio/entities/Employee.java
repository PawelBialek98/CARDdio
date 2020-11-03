package pl.lodz.pl.it.cardio.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
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
    private LocalDate birth;

    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false, nullable = false)
    @OneToOne(optional = false)
    @NotNull
    @Getter
    private User user;


    @ManyToMany
    @JoinTable(
            name = "employee_skill_t",
            joinColumns = @JoinColumn(
                    name = "employee_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "work_order_type_id", referencedColumnName = "id"))
    @Setter
    @Getter
    private  Collection<WorkOrderType> workOrderTypes;

    public Employee(){
    }

    public Employee(LocalDate birth, @NotNull User user, Collection<WorkOrderType> workOrderTypes) {
        this.birth = birth;
        this.user = user;
        this.workOrderTypes = workOrderTypes;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "birth=" + birth +
                "} " + super.toString();
    }
}
