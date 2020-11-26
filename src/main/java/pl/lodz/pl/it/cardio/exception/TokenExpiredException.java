package pl.lodz.pl.it.cardio.exception;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.entities.VerificationToken;

public class TokenExpiredException extends AppBaseException {

    public static final String TOKEN_EXPIRED_MESSAGE_KEY = "auth.message.expired";

    @Getter
    @Setter
    private Object object;

    public TokenExpiredException(String message) {
        super(message);
    }

    public static TokenExpiredException createTokenExpiredException(VerificationToken verificationToken) {
        TokenExpiredException tee = new TokenExpiredException(resourceBundle.getString(TOKEN_EXPIRED_MESSAGE_KEY));
        tee.setObject(verificationToken);
        return tee;
    }
}
