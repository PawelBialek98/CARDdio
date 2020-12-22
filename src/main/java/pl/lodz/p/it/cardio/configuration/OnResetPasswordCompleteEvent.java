package pl.lodz.p.it.cardio.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import pl.lodz.p.it.cardio.dto.UserDto.UserDto;

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
