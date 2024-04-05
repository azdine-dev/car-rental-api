package com.nbm.carrental.repository;

import com.nbm.carrental.entity.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long> {
    Agency findByUserEmail(String email);
    Optional<Agency> findByUserId(Long id);
}
