package com.nbm.carrental.service;

import com.nbm.carrental.entity.Agency;
import com.nbm.carrental.entity.AppRole;
import com.nbm.carrental.exception.RoleAlreadyExistingException;

import java.util.List;

public interface RoleService {

    AppRole addNewRole(AppRole role) throws RoleAlreadyExistingException;
    List<AppRole> getAllRoles();

}
