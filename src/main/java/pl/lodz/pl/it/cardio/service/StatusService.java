package pl.lodz.pl.it.cardio.service;

import pl.lodz.pl.it.cardio.entities.Status;
import pl.lodz.pl.it.cardio.exception.AppNotFoundException;

public interface StatusService {
    Status getStatusByCode(String code) throws AppNotFoundException;
}
