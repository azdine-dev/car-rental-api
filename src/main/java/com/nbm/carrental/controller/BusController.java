package com.nbm.carrental.controller;

import com.nbm.carrental.dto.BusDto;
import com.nbm.carrental.dto.CustomApiResponse;
import com.nbm.carrental.dto.RoleCt;
import com.nbm.carrental.dto.StatusCodeConstants;
import com.nbm.carrental.entity.Bus;
import com.nbm.carrental.entity.RoleEnum;
import com.nbm.carrental.exception.AgencyNotFoundException;
import com.nbm.carrental.exception.UserNotFoundException;
import com.nbm.carrental.service.IBusService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("buses")
@RequiredArgsConstructor
public class BusController {
    private final IBusService busService;

    @PreAuthorize("hasAuthority('ADMIN')") // Allow users with ADMIN  role
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ResponseEntity<List<Bus>> getAllBuses( @RequestParam(defaultValue = "0") Integer pageNo,
                                                  @RequestParam(defaultValue = "10") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        List<Bus> buses = busService.findAllBuses(pageable);
        return ResponseEntity.ok(buses);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user")
    public ResponseEntity<List<Bus>> getAllBusesForAuthenticatedUser(HttpServletRequest request, @RequestParam(defaultValue = "0") Integer pageNo,
                                                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Pageable pageable = PageRequest.of(pageNo, pageSize);
            List<Bus> buses = busService.findAllBusesForAuthenticatedUser(request, pageable);
            return ResponseEntity.ok(buses);
        } catch (UserNotFoundException e) {
            // Handle UserNotFoundException
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Secured(RoleCt.AGENCY)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Bus> getBusById(@PathVariable Long id) {
        Optional<Bus> bus = busService.getBusById(id);
        return bus.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity<CustomApiResponse<Bus>> createBus(@RequestBody BusDto busDto, HttpServletRequest request) {
        Bus createdBus = null;
        CustomApiResponse<Bus> response = new CustomApiResponse<>();
        try {
            createdBus = busService.createBus(busDto, request);
            response.setData(createdBus);
        } catch (UserNotFoundException | AgencyNotFoundException e) {
            response.setStatusCode(StatusCodeConstants.SOMETHING_WENT_WRONG);
            response.setMessage("Something Went Wrong");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((response));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body((response));
    }


    @Secured(RoleCt.AGENCY)
    @RequestMapping(method = RequestMethod.POST, value = "/{id}")
    public ResponseEntity<Bus> updateBus(@PathVariable Long id, @RequestBody BusDto busDto) {
        Bus updatedBus = busService.updateBus(id, busDto);
        if (updatedBus != null) {
            return ResponseEntity.ok(updatedBus);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Secured(RoleCt.AGENCY)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable Long id) {
        busService.deleteBus(id);
        return ResponseEntity.noContent().build();
    }

}







