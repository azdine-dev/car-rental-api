package com.nbm.carrental.repository;

import com.nbm.carrental.entity.Bus;
import com.nbm.carrental.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {

    Page<Bus> findByAgencyUser(User user, Pageable pageable);
}
