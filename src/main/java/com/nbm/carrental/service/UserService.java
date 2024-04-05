package com.nbm.carrental.service;

import com.nbm.carrental.dto.AgencySignupRequest;
import com.nbm.carrental.dto.JwtAuthenticationResponse;
import com.nbm.carrental.dto.SignInRequest;
import com.nbm.carrental.dto.SignUpRequest;
import com.nbm.carrental.entity.User;
import com.nbm.carrental.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    void addRoleToUser(String email, String roleName) throws UserNotFoundException, RoleNotFoundException, RoleAlreadyExistingException;
    JwtAuthenticationResponse registerUser(SignUpRequest userRequest) throws UserAlreadyExistingException, AgencyAlreadyExistException;
    JwtAuthenticationResponse authenticateUser(SignInRequest signInRequest) throws InvalidEmailOrPassword, UserNotEnabledException;
    JwtAuthenticationResponse registerAgency(AgencySignupRequest agencySignupRequest) throws SomethingWentWrongException, ExistedAgencyException;
//    Optional<User> findByEmail(String email);

    List<User> allUsers();

    void saveUserVerificationToken(User theUser, String verificationToken);

    User validateVerificationToken(String token) throws TokenException;

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    Optional<User> getUser(String tokenHeader);

    Optional<User> getUserByEmail(String email);

}
