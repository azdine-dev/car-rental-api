package com.nbm.carrental.service;

import com.nbm.carrental.entity.Agency;
import com.nbm.carrental.entity.AppRole;
import com.nbm.carrental.entity.User;
import com.nbm.carrental.repository.AgencyRepository;
import com.nbm.carrental.repository.AppRoleRepository;
import com.nbm.carrental.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AgencyServiceImpl implements AgencyService{

    private AgencyRepository agencyRepository;

    private UserRepository userRepository;

    private AppRoleRepository roleRepository;

     public AgencyServiceImpl(AgencyRepository agencyRepository,
                              AppRoleRepository roleRepository,
                              UserRepository userRepository){
        this.agencyRepository = agencyRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }
    @Override
    public Agency addNewAgency(Agency agency) {

         Agency agencySaved = agencyRepository.save(agency);
         return agencySaved;

    }

    @Override
    public void addRoleToAgency(String email, String roleName) {
        Agency agency = agencyRepository.findByUserEmail(email);
        AppRole role = roleRepository.findByRoleName(roleName);
        agency.getUser().getRoles().add(role);
    }


    @Override
    public Agency loadAgencyByEmail(String email) {
        return agencyRepository.findByUserEmail(email);
    }
}
