package pl.lodz.pl.it.cardio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.pl.it.cardio.entities.Status;
import pl.lodz.pl.it.cardio.repositories.StatusRepository;

import javax.transaction.Transactional;

@Transactional
@Service
public class StatusService {

    private final StatusRepository statusRepository;

    @Autowired
    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public Status getStatusByCode(String code){
        return statusRepository.findByCode(code);
    }
}
