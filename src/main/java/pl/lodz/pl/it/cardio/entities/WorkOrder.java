package pl.lodz.pl.it.cardio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "work_order_t")
@Getter
@Setter
public class WorkOrder extends BaseEntity {

    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    @NotNull
    private User customer;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column
    private String description;

    @JoinColumn(name = "employee_id", referencedColumnName = "user_id")
    @ManyToOne
    private Employee worker;

    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @ManyToOne
    private Status currentStatus;

    @JoinColumn(name = "position_id", referencedColumnName = "id")
    @ManyToOne
    private Position position;

}
