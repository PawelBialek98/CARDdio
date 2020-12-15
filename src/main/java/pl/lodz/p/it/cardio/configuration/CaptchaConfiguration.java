package pl.lodz.p.it.cardio.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "google.recaptcha.key")
@Data
public class CaptchaConfiguration {

    private String site;
    private String secret;
}
