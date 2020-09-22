package pl.lodz.pl.it.cardio.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import pl.lodz.pl.it.cardio.dto.UserDto;

import java.util.Locale;

@Getter
@Setter
public class OnResetPasswordCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private UserDto user;

    public OnResetPasswordCompleteEvent(
            UserDto user, Locale locale, String appUrl) {
        super(user);

        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }
}
