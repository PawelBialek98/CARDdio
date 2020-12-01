package pl.lodz.pl.it.cardio.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "work_order_flow_t")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class WorkOrderFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @JoinColumn(name = "status_from_id", referencedColumnName = "id")
    @ManyToOne
    private Status statusFrom;

    @JoinColumn(name = "status_to_id", referencedColumnName = "id")
    @ManyToOne
    private Status statusTo;

    @Column(name = "can_be_scheduled")
    private Boolean canBeScheduled;
}
