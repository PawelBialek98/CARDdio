package pl.lodz.pl.it.cardio.entities;

import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name="user_t")
@SecondaryTable(name = "user_details_t", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "user_id")})
//@Inheritance(strategy=InheritanceType.JOINED)
//@DiscriminatorColumn(name = "roles")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class User extends BaseEntity {

    @Column(name = "first_name", table = "user_details_t")
    @NonNull
    private String firstName;

    @Column(name = "last_name", table = "user_details_t")
    @NonNull
    private String lastName;

    @Column
    @NonNull
    @Email
    @Pattern(regexp = "^[^\\s\\\\@]+@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\\.){1,11}[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?$")
    private String email;

    @Column(nullable = false)
    @NonNull
    private String password;

    @Column(name = "phone_number", table = "user_details_t")
    @NonNull
    @Pattern(regexp = "\\d{9}")
    private String phoneNumber;

    @Column
    private Boolean activated = false;

    @Column
    private Boolean locked = false;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "invalid_login_attempts", nullable = false, columnDefinition = "integer default 0")
    @NotNull
    private int invalidLoginAttempts = 0;

    @ManyToMany(fetch = FetchType.EAGER)
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
                "firstName='" + firstName + '\'' +
                ", lastname='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", activated=" + activated +
                ", locked=" + locked +
                ", invalidLoginAttempts=" + invalidLoginAttempts +
                ", roles=" + roles +
                "} " + super.toString();
    }
}
