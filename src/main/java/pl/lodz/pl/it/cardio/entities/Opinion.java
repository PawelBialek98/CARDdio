package pl.lodz.pl.it.cardio.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "opinion_t")
@Data
public class Opinion extends BaseEntity{

    @Column
    @Min(1)
    @Max(5)
    private int rating;

    @Column
    private String description;

    @JoinColumn(name = "work_order_id", referencedColumnName = "id", updatable = false, nullable = false)
    @OneToOne(optional = false)
    @NotNull
    private WorkOrder workOrder;
}
