package com.nbm.carrental.controller;

import com.nbm.carrental.dto.*;
import com.nbm.carrental.entity.*;
import com.nbm.carrental.event.RegistrationEventPublisher;
import com.nbm.carrental.exception.*;
import com.nbm.carrental.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final RegistrationEventPublisher publisher;


    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<CustomApiResponse<JwtAuthenticationResponse>> registerUser(@RequestBody SignUpRequest user, final HttpServletRequest request) {

               CustomApiResponse<JwtAuthenticationResponse> response= new CustomApiResponse<>();
        JwtAuthenticationResponse jwtResponse;
        try {
                jwtResponse = userService.registerUser(user);

        } catch (UserAlreadyExistingException e) {
            response.setStatusCode(StatusCodeConstants.EMAIL_ALREADY_EXIST);
            response.setMessage("Email already exists!");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (AgencyAlreadyExistException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(StatusCodeConstants.SUCCESS_EMAIL_VERIFICATION);
        response.setData(jwtResponse);
        response.setMessage("User registered successfully," +
                " we have sent you a verification Email," +
                " to be able to Login");

        publisher.publishEventAsync(jwtResponse.getUser(), applicationUrl(request));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register/agency")
    @Transactional
    public ResponseEntity<CustomApiResponse<JwtAuthenticationResponse>> registerAgency(@RequestBody AgencySignupRequest signUpRequest, final HttpServletRequest request) {
        CustomApiResponse<JwtAuthenticationResponse> response= new CustomApiResponse<>();
        JwtAuthenticationResponse jwtResponse;

        try {
            jwtResponse = userService.registerAgency(signUpRequest);
        } catch (SomethingWentWrongException e) {
            throw new RuntimeException(e);
        } catch (ExistedAgencyException e) {
            response.setStatusCode(StatusCodeConstants.AGENCY_ALREADY_EXISTED);
            response.setMessage("Agency already exists!");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        String message;
        int statusCode;
        if(jwtResponse.getUser().getEnabled()){
          message = "Agency registered successfully";
          statusCode = StatusCodeConstants.SUCCESS;
        }else{
            message = "Agency registered successfully," +
                    " please verify your email, to be able to log in";
            statusCode = StatusCodeConstants.USER_NOT_ENABLED;
        }

        response.setStatusCode(statusCode);
        response.setData(jwtResponse);
        response.setMessage(message);

        publisher.publishEventAsync(jwtResponse.getUser(), applicationUrl(request));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/verifyEmail")
    public ResponseEntity<CustomApiResponse<User>> verifyEmail(@RequestParam("token") String token){
        User savedUser;
        CustomApiResponse<User> response = new CustomApiResponse<>();
        try {
             savedUser = userService.validateVerificationToken(token);
        } catch (TokenException e) {
            response.setStatusCode(e.getStatusCode());
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }
        response.setStatusCode(StatusCodeConstants.SUCCESS);
        response.setData(savedUser);
        response.setMessage("Email verified successfully. Now you can login to your account");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    @RequestMapping(method = RequestMethod.POST, value = "/addRoleToUser")
    public ResponseEntity<String> addRoleToUser(@RequestBody RoleUserForm roleUser){
        try {
            userService.addRoleToUser(roleUser.getUserName(), roleUser.getRoleName());
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        } catch (RoleNotFoundException e) {
            return new ResponseEntity<>("Role Not Found", HttpStatus.NOT_FOUND);
        }  catch (RoleAlreadyExistingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Role added to user successfully!", HttpStatus.OK);
    }



    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<CustomApiResponse<JwtAuthenticationResponse>> login(@RequestBody SignInRequest request) {

        CustomApiResponse<JwtAuthenticationResponse>
                response = new CustomApiResponse<>();
        JwtAuthenticationResponse jwtResponse ;
        try {
            jwtResponse = userService.authenticateUser(request);
        } catch (InvalidEmailOrPassword | UserNotEnabledException e) {
            int statusCode = e instanceof InvalidEmailOrPassword ?
                    StatusCodeConstants.INVALID_EMAIL_PASSWD:
                    StatusCodeConstants.USER_NOT_ENABLED;
            response.setStatusCode(statusCode);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

       response.setStatusCode(StatusCodeConstants.SUCCESS);
       response.setData(jwtResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/allUsers")
    public ResponseEntity<List<User>> allUsers(){
        List<User> users = userService.allUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        userService.refreshToken(request, response);
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

}
