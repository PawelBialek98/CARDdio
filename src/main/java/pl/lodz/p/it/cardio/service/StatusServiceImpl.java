package pl.lodz.p.it.cardio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.cardio.entities.Status;
import pl.lodz.p.it.cardio.repositories.StatusRepository;
import pl.lodz.p.it.cardio.exception.AppNotFoundException;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Override
    public Status getStatusByCode(String code) throws AppNotFoundException {
        return statusRepository.findByCode(code).orElseThrow(AppNotFoundException::createStatusNotFoundException);
    }
}
