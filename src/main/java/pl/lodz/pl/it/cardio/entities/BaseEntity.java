package pl.lodz.pl.it.cardio.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor
@Getter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Version
    private final Integer version = 1;

    @Column(name = "business_key", nullable = false)
    private final UUID businessKey = UUID.randomUUID();

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
