package pl.lodz.p.it.cardio.events.statusChange;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import pl.lodz.p.it.cardio.dto.UserDto;
import pl.lodz.p.it.cardio.entities.WorkOrder;

import java.util.Locale;

@Getter
@Setter
public class OrderStatusChangeEvent extends ApplicationEvent {
    private WorkOrder workOrder;
    private Locale locale;
    private String messagePrefix;

    public OrderStatusChangeEvent(WorkOrder workOrder, Locale locale, String messagePrefix) {
        super(workOrder);
        this.workOrder = workOrder;
        this.locale = locale;
        this.messagePrefix = messagePrefix;
    }
}
