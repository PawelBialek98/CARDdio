package pl.lodz.pl.it.cardio.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "status_t")
@Data
public class Status extends BaseEntity{

    @Column
    private String name;

    @Column
    private String code;
}
