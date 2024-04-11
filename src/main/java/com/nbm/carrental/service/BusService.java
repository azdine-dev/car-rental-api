package com.nbm.carrental.service;

import com.nbm.carrental.dto.BusDto;
import com.nbm.carrental.entity.Agency;
import com.nbm.carrental.entity.Bus;
import com.nbm.carrental.entity.BusImage;
import com.nbm.carrental.entity.User;
import com.nbm.carrental.exception.AgencyNotFoundException;
import com.nbm.carrental.exception.UserNotFoundException;
import com.nbm.carrental.filter.JwtAuthenticationFilter;
import com.nbm.carrental.repository.AgencyRepository;
import com.nbm.carrental.repository.BusRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusService implements IBusService {

    private final BusRepository busRepository;
    private final UserService userService;
    private final AgencyRepository agencyRepository;

    @Override
    public List<Bus> findAllBuses(Pageable pageable) {
        Page<Bus> buses = busRepository.findAll(pageable);
        return buses.getContent();
    }

    @Override
    public Bus createBus(BusDto busDto, HttpServletRequest request) throws UserNotFoundException, AgencyNotFoundException {
        String userEmail = (String) request.getAttribute(JwtAuthenticationFilter.USER_EMAIL_ATTRIBUTE);
        Optional<User> user = userService.getUserByEmail(userEmail);

        if (userEmail == null || userEmail.isEmpty() || user.isEmpty()) {
           throw new UserNotFoundException();
        }

        // Fetch userId from your database using userEmail
        Long userId = user.get().getId();
        Bus bus = convertToEntity(busDto);

        // Get the Agency by User ID
        Optional<Agency> optionalAgency = agencyRepository.findByUserId(userId);
        if (optionalAgency.isPresent()) {
            Agency agency = optionalAgency.get();
            bus.setAgency(agency);
        } else {
            throw new AgencyNotFoundException();
        }

        return busRepository.save(bus);
    }

    @Override
    public List<Bus> findAllBusesForAuthenticatedUser(HttpServletRequest request, Pageable pageable) throws UserNotFoundException {
        String userEmail = (String) request.getAttribute(JwtAuthenticationFilter.USER_EMAIL_ATTRIBUTE);
        Optional<User> userOptional = userService.getUserByEmail(userEmail);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        User user = userOptional.get();
        Page<Bus> buses = busRepository.findByAgencyUser(user, pageable);
        return buses.getContent();
    }

    @Override
    public Optional<Bus> getBusById(Long id) {
        Optional<Bus> busOptional = busRepository.findById(id);
        return busOptional;
    }


    @Override
    public Bus updateBus(Long id, BusDto updatedBusDto) {
        Optional<Bus> busOptional = busRepository.findById(id);
        if (busOptional.isPresent()) {
            Bus existingBus = busOptional.get();
            existingBus.setName(updatedBusDto.getName());
            existingBus.setBusNumber(updatedBusDto.getBusNumber());
            existingBus.setModel(updatedBusDto.getModel());
            existingBus.setLatitude(updatedBusDto.getLatitude());
            existingBus.setLongitude(updatedBusDto.getLongitude());
            existingBus.setAmenities(updatedBusDto.getAmenities());
            existingBus.setCapacity(updatedBusDto.getCapacity());
            existingBus.setDescription(updatedBusDto.getDescription());

            // Update BusImages
            List<BusImage> updatedImages = updatedBusDto.getImageUrls().stream()
                    .map(imageUrl -> {
                        BusImage busImage = new BusImage();
                        busImage.setImageUrl(imageUrl);
                        return busImage;
                    })
                    .collect(Collectors.toList());
            existingBus.setImages(updatedImages);

            return busRepository.save(existingBus);
        }
        return null;
    }

    @Override
    public void deleteBus(Long id) {
        busRepository.deleteById(id);
    }

    private BusDto convertToDto(Bus bus) {
        BusDto busDto = new BusDto();
        busDto.setId(bus.getId());
        busDto.setName(bus.getName());
        busDto.setBusNumber(bus.getBusNumber());
        busDto.setModel(bus.getModel());
        busDto.setLatitude(bus.getLatitude());
        busDto.setLongitude(bus.getLongitude());
        busDto.setAmenities(bus.getAmenities());
        busDto.setImageUrls(bus.getImages().stream().map(BusImage::getImageUrl).collect(Collectors.toList()));
        busDto.setCapacity(bus.getCapacity());
        busDto.setDescription(bus.getDescription());
        return busDto;
    }

    private Bus convertToEntity(BusDto busDto) {
        Bus bus = new Bus();
        bus.setName(busDto.getName());
        bus.setBusNumber(busDto.getBusNumber());
        bus.setModel(busDto.getModel());
        bus.setLatitude(busDto.getLatitude());
        bus.setLongitude(busDto.getLongitude());
        bus.setAmenities(busDto.getAmenities());
        bus.setCapacity(busDto.getCapacity());
        bus.setDescription(busDto.getDescription());

        // Convert image URLs to BusImages
        List<BusImage> images = busDto.getImageUrls().stream()
                .map(imageUrl -> {
                    BusImage busImage = new BusImage();
                    busImage.setImageUrl(imageUrl);
                    return busImage;
                })
                .collect(Collectors.toList());
        bus.setImages(images);

        return bus;
    }
}
