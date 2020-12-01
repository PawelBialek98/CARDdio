package pl.lodz.pl.it.cardio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.pl.it.cardio.entities.Status;
import pl.lodz.pl.it.cardio.exception.AppNotFoundException;
import pl.lodz.pl.it.cardio.repositories.StatusRepository;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Override
    public Status getStatusByCode(String code) throws AppNotFoundException {
        return statusRepository.findByCode(code).orElseThrow(AppNotFoundException::createStatusNotFoundException);
    }
}
