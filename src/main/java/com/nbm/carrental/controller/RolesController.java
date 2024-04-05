package com.nbm.carrental.controller;

import com.nbm.carrental.entity.AppRole;
import com.nbm.carrental.exception.RoleAlreadyExistingException;
import com.nbm.carrental.repository.AppRoleRepository;
import com.nbm.carrental.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("roles") // Specify the base mapping here
public class RolesController {
    private RoleService roleService;

    public RolesController( RoleService roleService){
        this.roleService = roleService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "")
    public ResponseEntity<String> addNewRole(@RequestBody AppRole role){
        try {
            AppRole appRole = roleService.addNewRole(role);
        } catch (RoleAlreadyExistingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Role added successfully!", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public ResponseEntity<List<AppRole>> getAllRoles(){
        List<AppRole> appRoles = roleService.getAllRoles();
        return new ResponseEntity<>(appRoles, HttpStatus.OK);
    }
}
