package pl.lodz.p.it.cardio.service;

import pl.lodz.p.it.cardio.entities.Status;
import pl.lodz.p.it.cardio.exception.AppNotFoundException;

public interface StatusService {
    Status getStatusByCode(String code) throws AppNotFoundException;
}
