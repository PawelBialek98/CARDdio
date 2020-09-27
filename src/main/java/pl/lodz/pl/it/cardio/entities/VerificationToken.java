package pl.lodz.pl.it.cardio.entities;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.lodz.pl.it.cardio.dto.UserDto;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "verification_token_t")
public class VerificationToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Column
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "expiry_date")
    private Date expiryDate;

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user =user;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public VerificationToken() {
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VerificationToken{");
        sb.append("id=").append(id);
        sb.append(", token='").append(token).append('\'');
        sb.append(", user=").append(user);
        sb.append(", expiryDate=").append(expiryDate);
        sb.append('}');
        return sb.toString();
    }

}