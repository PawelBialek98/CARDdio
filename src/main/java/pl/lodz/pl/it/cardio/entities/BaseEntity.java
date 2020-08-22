package pl.lodz.pl.it.cardio.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Version
    private Integer version;

    //@Convert("uuidConverter")
    //@TypeConverter(name = "uuidConverter", dataType = Object.class, objectType = UUID.class)
    @Column(name = "business_key", nullable = false)
    @Getter
    private UUID businessKey;
    //private final UUID businessKey = UUID.randomUUID(); <- nie działa (na razie)

    public UUID getBusinessKey() {
        return businessKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(businessKey, that.businessKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessKey);
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", version=" + version +
                ", businessKey=" + businessKey +
                '}';
    }
}
