package pl.lodz.p.it.cardio.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "work_order_t")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class WorkOrder extends BaseEntity {

    @JoinColumn(name = "customer_id", referencedColumnName = "id")//, nullable = false, updatable = false)
    @ManyToOne()//optional = false)
    private User customer;

    @Column(name = "start_date")
    //@NotNull
    private Date startDateTime;

    @Column(name = "end_date")
    private Date endDateTime;

    @Column
    private String description;

    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne
    private Employee employee;

    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @ManyToOne
    private Status currentStatus;

    @JoinColumn(name = "type_id", referencedColumnName = "id")
    @ManyToOne
    private WorkOrderType workOrderType;

    /**
     * Metoda wylicza czy można anulować rezerwację na daną naprawę.
     * Można anulować maksymalnie do 1h przed faktyczną wizytą
     *
     * @return flaga mówiąca czy można anulować rezewację
     */
    public boolean canBeCancelled(){
        return startDateTime.getTime() - new Date().getTime() > 3600000;
    }
}
