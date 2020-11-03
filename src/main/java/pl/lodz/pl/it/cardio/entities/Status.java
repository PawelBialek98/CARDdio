package pl.lodz.pl.it.cardio.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "status_t")
@Data
public class Status extends BaseEntity{

    @Column
    private String name;

    @Column
    private String code;

    @Column
    private String colour;

    @Column(name = "status_type")
    private String statusType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "statusFrom")
    private Collection<WorkOrderFlow> workOrderFlow;
}
