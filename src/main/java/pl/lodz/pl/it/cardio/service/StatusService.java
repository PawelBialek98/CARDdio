package pl.lodz.pl.it.cardio.service;

import pl.lodz.pl.it.cardio.entities.Status;

public interface StatusService {
    Status getStatusByCode(String code);
}
