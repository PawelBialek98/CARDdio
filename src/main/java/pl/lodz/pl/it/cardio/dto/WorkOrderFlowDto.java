package pl.lodz.pl.it.cardio.dto;

import lombok.Data;
import pl.lodz.pl.it.cardio.entities.Status;

import javax.persistence.*;

@Data
public class WorkOrderFlowDto {

    private SimpleStatusDto statusFrom;

    private SimpleStatusDto statusTo;

    private Boolean canBeScheduled;
}
