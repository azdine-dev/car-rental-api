package com.nbm.carrental.controller;

import com.nbm.carrental.dto.*;
import com.nbm.carrental.entity.RoleUserForm;
import com.nbm.carrental.entity.User;
import com.nbm.carrental.event.RegistrationEventPublisher;
import com.nbm.carrental.exception.*;
import com.nbm.carrental.service.AgencyService;
import com.nbm.carrental.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ResponseEntity<CustomApiResponse<User>> getAuthenticatedUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION)
                                                               String authorizationHeader){
        Optional<User> user = userService.getUser(authorizationHeader);
        if (user.isPresent()) {
            CustomApiResponse<User> response = new CustomApiResponse<>();
            response.setStatusCode(StatusCodeConstants.SUCCESS);
            response.setData(user.get());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        CustomApiResponse<User> errorResponse = new CustomApiResponse<>();
        errorResponse.setStatusCode(StatusCodeConstants.UNAUTHORIZED);
        errorResponse.setMessage("Unauthorized");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
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


}
