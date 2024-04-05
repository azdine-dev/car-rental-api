package com.nbm.carrental;

import com.nbm.carrental.dto.AgencySignupRequest;
import com.nbm.carrental.dto.JwtAuthenticationResponse;
import com.nbm.carrental.dto.SignInRequest;
import com.nbm.carrental.dto.SignUpRequest;
import com.nbm.carrental.entity.AppRole;
import com.nbm.carrental.entity.User;
import com.nbm.carrental.exception.RoleAlreadyExistingException;
import com.nbm.carrental.exception.SomethingWentWrongException;
import com.nbm.carrental.exception.UserAlreadyExistingException;
import com.nbm.carrental.repository.UserRepository;
import com.nbm.carrental.service.RoleService;
import com.nbm.carrental.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootApplication
public class CarRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentalApplication.class, args);
    }


    @Bean
    CommandLineRunner start(UserService userService, RoleService roleService, UserRepository userRepository) {
        return args -> {

            try {
                roleService.addNewRole(new AppRole(null, "USER"));
                roleService.addNewRole(new AppRole(null, "ADMIN"));
                roleService.addNewRole(new AppRole(null, "AGENCY"));
            }catch (RoleAlreadyExistingException e){
                e.printStackTrace();
            }
             LocalDateTime now = LocalDateTime.now();

            try {
               JwtAuthenticationResponse response=
                       userService.registerAgency(new AgencySignupRequest("omnicar2024@gmail.com","123456"));
               User user = response.getUser();
               user.setEnabled(true);
                JwtAuthenticationResponse response2=
                        userService.registerAgency(new AgencySignupRequest("mobilia@gmail.com","123456"));
                User user2 = response2.getUser();
                user2.setEnabled(true);
                userRepository.save(user);
                userRepository.save(user2);

            } catch (SomethingWentWrongException e) {
                throw new RuntimeException(e);
            }

            userService.addRoleToUser("omnicar2024@gmail.com", "ADMIN");
//            userService.addRoleToUser("omnicar2024@gmail.com" ,"AGENCY");

        };
    }

}
