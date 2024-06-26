package com.nbm.carrental.repository;


import com.nbm.carrental.entity.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository  extends JpaRepository<AppRole, Long> {
    AppRole findByRoleName(String roleName);
}
