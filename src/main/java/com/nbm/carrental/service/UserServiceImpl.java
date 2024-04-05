package com.nbm.carrental.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbm.carrental.dto.AgencySignupRequest;
import com.nbm.carrental.dto.JwtAuthenticationResponse;
import com.nbm.carrental.dto.SignInRequest;
import com.nbm.carrental.dto.SignUpRequest;
import com.nbm.carrental.entity.*;
import com.nbm.carrental.exception.*;
import com.nbm.carrental.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final ObjectMapper mapper;
    private final AgencyService agencyService;
    private final UserDetailsService userDetailsService;

    @Override
    public void addRoleToUser(String email, String roleName) throws
            UserNotFoundException,
            RoleNotFoundException,
            RoleAlreadyExistingException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        AppRole role = roleRepository.findByRoleName(roleName);

        if (role == null) {
            throw new RoleNotFoundException();
        } else if (userOptional.isPresent()) {
            User user = userOptional.get();
            boolean roleAlreadyAdded = user.getRoles().stream()
                    .anyMatch(r -> r.getRoleName().equalsIgnoreCase(roleName));
            if (roleAlreadyAdded) {
                throw new RoleAlreadyExistingException("User already has that role: " + role.getRoleName());
            }

            // Create a new mutable list of roles
            List<AppRole> updatedRoles = new ArrayList<>(user.getRoles());
            updatedRoles.add(role);

            // Set the new roles to the user
            user.setRoles(updatedRoles);

            // Save the updated user
            userRepository.save(user);
        } else {
            throw new UserNotFoundException();
        }
    }


    public JwtAuthenticationResponse registerUser(SignUpRequest userRequest) throws UserAlreadyExistingException, AgencyAlreadyExistException {
        Optional<User> userOptional = userRepository.findByEmail(userRequest.getEmail());
        boolean userExist = userOptional.isPresent();

        if(userExist){

            throw new UserAlreadyExistingException("User email with that email already registered");
        }
        AppRole userRole = roleRepository.findByRoleName("USER");
        String encryptedPassword = passwordEncoder.encode(userRequest.getPassword());
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .firstname(userRequest.getFirstName())
                .lastname(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(encryptedPassword)
                .createdAt(now)
                .updatedAt(now)
                .roles(List.of(userRole))
                .enabled(false)
                .locked(false)
                .build();

        var savedUser = userRepository.save(user);
        return JwtAuthenticationResponse.
                builder()
                .user(savedUser)
                .build();
    }

    @Override
    public JwtAuthenticationResponse authenticateUser(SignInRequest request) throws InvalidEmailOrPassword, UserNotEnabledException {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new InvalidEmailOrPassword());

        if(!user.getEnabled()){
            throw new UserNotEnabledException();
        }

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);


        return JwtAuthenticationResponse.builder()
                .token(jwtToken)
                .user(user)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);

    }

    @Override
    public JwtAuthenticationResponse registerAgency( AgencySignupRequest agencySignupRequest) throws SomethingWentWrongException, ExistedAgencyException {
        AppRole agencyRole = roleRepository.findByRoleName("AGENCY");
        JwtAuthenticationResponse jwtResponse = null;
        SignUpRequest userRequest = SignUpRequest
                .builder()
                .email(agencySignupRequest.getEmail())
                .password(agencySignupRequest.getPassword())
                .build();
        LocalDateTime now = LocalDateTime.now();
        Agency agency = Agency
                         .builder()
                         .build();


        try {
            jwtResponse = registerUser(userRequest);
            addRoleToUser(jwtResponse.getUser().getEmail(),agencyRole.getRoleName());
            agency.setUser(jwtResponse.getUser());
            agencyService.addNewAgency(agency);
        } catch (UserAlreadyExistingException e) {
            Optional<User> userOptional = userRepository.findByEmail(userRequest.getEmail());
            User user = userOptional.get();
            boolean agencyAlreadyExist = user.getRoles().stream()
                    .anyMatch(r -> r.getRoleName().equalsIgnoreCase("AGENCY"));
            if(agencyAlreadyExist){
                throw new ExistedAgencyException();
            }
            log.info("Adding Agency Role to existing user");
        } catch (UserNotFoundException | RoleNotFoundException e) {
            throw new SomethingWentWrongException();
        } catch (RoleAlreadyExistingException | AgencyAlreadyExistException e) {
            throw new ExistedAgencyException();
        }
        return jwtResponse;

    }



    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public User validateVerificationToken(String theToken) throws TokenException {
        VerificationToken token = verificationTokenRepository.findByToken(theToken);
        if(token == null){
           throw new VerificationTokenNotFound();
        }
        User user = token.getUser();
        if(user!=null && user.getEnabled()){
            throw new UserAlreadyVerified();
        }

        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            verificationTokenRepository.delete(token);
           throw new VerificationTokenExpired();
        }
        user.setEnabled(true);
        return  userRepository.save(user);

    }


    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = JwtAuthenticationResponse.builder()
                        .token(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                mapper.writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Override
    public Optional<User> getUser(String authorizationHeader) {
        Optional<User> user = Optional.empty();
        if(StringUtils.hasLength(authorizationHeader) &&
                StringUtils.startsWithIgnoreCase(authorizationHeader,"Bearer ")){
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            String userEmail = jwtService.extractUsername(token);

            UserDetails userDetails =userDetailsService
                    .loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(token, userDetails)) {

                user = findByEmail(userEmail);

            }

        }
        
        return user;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
