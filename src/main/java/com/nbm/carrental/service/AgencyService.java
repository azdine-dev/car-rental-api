package com.nbm.carrental.service;

import com.nbm.carrental.entity.Agency;
import com.nbm.carrental.entity.User;

public interface AgencyService {

    Agency addNewAgency(Agency agency);
    void addRoleToAgency(String agencyName, String roleName);

    Agency loadAgencyByEmail(String email);
}
