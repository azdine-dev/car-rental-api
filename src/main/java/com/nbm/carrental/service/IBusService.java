package com.nbm.carrental.service;

import com.nbm.carrental.dto.BusDto;
import com.nbm.carrental.entity.Bus;
import com.nbm.carrental.exception.AgencyNotFoundException;
import com.nbm.carrental.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IBusService {
    List<Bus> findAllBuses(Pageable pageable);

    Bus createBus(BusDto busDto, HttpServletRequest request) throws UserNotFoundException, AgencyNotFoundException;

    List<Bus> findAllBusesForAuthenticatedUser(HttpServletRequest request, Pageable pageable) throws UserNotFoundException;

    Optional<Bus> getBusById(Long id);

    Bus updateBus(Long id, BusDto updatedBusDto);

    void deleteBus(Long id);
}
