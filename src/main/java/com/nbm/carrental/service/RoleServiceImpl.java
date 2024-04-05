package com.nbm.carrental.service;

import com.nbm.carrental.entity.Agency;
import com.nbm.carrental.entity.AppRole;
import com.nbm.carrental.exception.RoleAlreadyExistingException;
import com.nbm.carrental.repository.AppRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private AppRoleRepository roleRepository;

    public RoleServiceImpl(AppRoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    public AppRole addNewRole(AppRole role) throws RoleAlreadyExistingException {
        if(roleRepository.findByRoleName(role.getRoleName()) !=null){
            throw new RoleAlreadyExistingException("Role Already exist");
        }
        return roleRepository.save(role);
    }

    @Override
    public List<AppRole> getAllRoles() {
        return roleRepository.findAll();
    }
}
