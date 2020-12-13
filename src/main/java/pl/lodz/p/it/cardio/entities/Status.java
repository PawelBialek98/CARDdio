package pl.lodz.p.it.cardio.entities;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Status{");
        sb.append("name='").append(name).append('\'');
        sb.append(", code='").append(code).append('\'');
        sb.append(", colour='").append(colour).append('\'');
        sb.append(", statusType='").append(statusType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
