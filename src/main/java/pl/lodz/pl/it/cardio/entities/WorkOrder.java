package pl.lodz.pl.it.cardio.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "work_order_t")
@Getter
@Setter
@EqualsAndHashCode
public class WorkOrder extends BaseEntity {

    @JoinColumn(name = "customer_id", referencedColumnName = "id")//, nullable = false, updatable = false)
    @ManyToOne()//optional = false)
    private User customer;

    @Column(name = "start_date")
    //@NotNull
    private Date startDateTime;

    @Column
    private String description;

    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne
    private Employee worker;

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
