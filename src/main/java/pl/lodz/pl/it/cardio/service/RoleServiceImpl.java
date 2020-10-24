package pl.lodz.pl.it.cardio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.pl.it.cardio.entities.Role;
import pl.lodz.pl.it.cardio.repositories.RoleRepository;

import javax.transaction.Transactional;
import java.util.Collection;

@Transactional
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Collection<Role> getRoleByCode(String code){
        return roleRepository.findByCode(code).orElseThrow();
    }

    @Override
    public Collection<Role> getAllRolesByCodes(Collection<String> codes){
        return roleRepository.findAllByCodeIn(codes);
    }

    @Override
    public Collection<Role> getAllRolesByNames(Collection<String> names){
        return roleRepository.findAllByNameIn(names);
    }

    @Override
    public Collection<Role> getAllRoles() {
        return roleRepository.findAllByIsEnabledIsTrue();
    }
}
