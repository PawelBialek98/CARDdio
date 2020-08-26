package pl.lodz.pl.it.cardio.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "opinion_t")
public class Opinion extends BaseEntity{

    @Column
    @Min(1)
    @Max(5)
    @Getter
    @Setter
    private int rating;

    @Column
    @Getter
    @Setter
    private String description;

    @JoinColumn(name = "work_order_id", referencedColumnName = "id", updatable = false, nullable = false)
    @OneToOne(optional = false)
    @NotNull
    @Getter
    @Setter
    private WorkOrder workOrder;
}
