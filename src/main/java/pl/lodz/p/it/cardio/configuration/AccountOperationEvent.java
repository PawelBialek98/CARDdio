package pl.lodz.p.it.cardio.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import pl.lodz.p.it.cardio.dto.UserDto;

import java.util.Locale;

@Getter
@Setter
public class AccountOperationEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private UserDto user;
    private String messagePrefix;

    public AccountOperationEvent(
            UserDto user, Locale locale, String appUrl, String messagePrefix) {
        super(user);

        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
        this.messagePrefix = messagePrefix;
    }
}
