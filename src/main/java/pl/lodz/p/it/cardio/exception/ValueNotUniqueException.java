package pl.lodz.p.it.cardio.exception;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.cardio.entities.User;
import pl.lodz.p.it.cardio.entities.WorkOrderType;

public class ValueNotUniqueException extends AppBaseException {

    public static final String EMAIL_MESSAGE_KEY = "exception.emailNotUnique";
    public static final String ORDER_TYPE_CODE_MESSAGE_KEY = "exception.orderTypeCodeNotUnique";

    @Getter
    @Setter
    private Object object;

    public ValueNotUniqueException(String message) {
        super(message);
    }

    public static ValueNotUniqueException createEmailNotUniqueException(User user) {
        ValueNotUniqueException nue = new ValueNotUniqueException(resourceBundle.getString(EMAIL_MESSAGE_KEY));
        nue.setObject(user);
        return nue;
    }

    public static ValueNotUniqueException createWorkOrderTypeCodeNotUniqueException(WorkOrderType wot) {
        ValueNotUniqueException nue = new ValueNotUniqueException(resourceBundle.getString(ORDER_TYPE_CODE_MESSAGE_KEY));
        nue.setObject(wot);
        return nue;
    }
}
