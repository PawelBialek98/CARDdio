package pl.lodz.pl.it.cardio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.pl.it.cardio.entities.Role;
import pl.lodz.pl.it.cardio.repositories.RoleRepository;

import javax.transaction.Transactional;
import java.util.Collection;

@Transactional
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Collection<Role> getRoleByCode(String code){
        return roleRepository.findByCode(code).orElseThrow();
    }
}
