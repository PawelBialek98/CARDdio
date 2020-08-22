package pl.lodz.pl.it.cardio.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Collection;

@Entity
@Table(name="user_t")
@SecondaryTable(name = "user_details_t", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "user_id")})
//@Inheritance(strategy=InheritanceType.JOINED)
//@DiscriminatorColumn(name = "roles")
@NoArgsConstructor
@RequiredArgsConstructor
public class User extends BaseEntity {

    @Column(name = "first_name", table = "user_details_t")
    @NonNull
    private String firstName;

    @Column(name = "last_name", table = "user_details_t")
    @NonNull
    private String lastname;

    @Column
    @NonNull
    @Pattern(regexp = "^[^\\s\\\\@]+@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\\.){1,11}[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?$")
    private String email;

    @Column(nullable = false)
    @NonNull
    private String password;

    @Column(name = "phone_number", table = "user_details_t")
    @NonNull
    @Pattern(regexp = "\\d{9}")
    private String phoneNumber;

    @ManyToMany
    @JoinTable(
            name = "user_role_t",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;


    //@OneToOne(cascade = CascadeType.REFRESH, mappedBy = "user")
    //private Employee employee;

    //TODO do ukrycia dane biznesowe
    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", roles=" + roles +
                "} " + super.toString();
    }
}
