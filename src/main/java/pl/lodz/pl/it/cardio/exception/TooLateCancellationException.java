package pl.lodz.pl.it.cardio.exception;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.pl.it.cardio.entities.User;
import pl.lodz.pl.it.cardio.entities.WorkOrder;

public class TooLateCancellationException extends AppBaseException{

    public static final String TOO_LATE_CANCELLATION_MESSAGE_KEY = "exception.tooLateCancallation";

    @Getter
    @Setter
    private Object object;

    public TooLateCancellationException(String message) {
        super(message);
    }

    public TooLateCancellationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static TooLateCancellationException createTooLateCancellationException(WorkOrder workOrder) {
        TooLateCancellationException nue = new TooLateCancellationException(resourceBundle.getString(TOO_LATE_CANCELLATION_MESSAGE_KEY));
        nue.setObject(workOrder);
        return nue;
    }

}
