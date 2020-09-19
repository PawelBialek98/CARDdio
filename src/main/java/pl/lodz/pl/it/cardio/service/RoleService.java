package pl.lodz.pl.it.cardio.service;

import pl.lodz.pl.it.cardio.entities.Role;

import java.util.Collection;

public interface RoleService {
    Collection<Role> getRoleByCode(String code);
}
