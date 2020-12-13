package pl.lodz.p.it.cardio.entities;

import lombok.Getter;
import lombok.Setter;

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

    @Column
    private String type;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "expiry_date")
    private Date expiryDate;

    public VerificationToken(String token, User user, String type) {
        this.token = token;
        this.user = user;
        this.type = type;
        this.expiryDate = calculateExpiryDate();

    }

    public VerificationToken() {
    }

    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, VerificationToken.EXPIRATION);
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