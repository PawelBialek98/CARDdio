package pl.lodz.pl.it.cardio.exception;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.pl.it.cardio.entities.WorkOrder;

public class OrderInterfereException extends AppBaseException {

    public static final String INTERFERE_MESSAGE_KEY = "exception.workOrderInterfere";

    @Getter
    @Setter
    private Class objectClass;

    public OrderInterfereException(String message) {
        super(message);
    }

    public OrderInterfereException(String message, Throwable cause) {
        super(message, cause);
    }

    public static OrderInterfereException createOrderInterfereException(){
        OrderInterfereException oie = new OrderInterfereException(resourceBundle.getString(INTERFERE_MESSAGE_KEY));
        oie.setObjectClass(WorkOrder.class);
        return oie;
    }
}
