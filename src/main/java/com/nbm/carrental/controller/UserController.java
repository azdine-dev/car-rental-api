package com.nbm.carrental.controller;

import com.nbm.carrental.entity.User;
import com.nbm.carrental.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>("Username already exists!", HttpStatus.BAD_REQUEST);
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            return new ResponseEntity<>("Email already exists!", HttpStatus.BAD_REQUEST);
        }

        userService.registerUser(user);
        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }

    @PostMapping("/register/agency")
    public ResponseEntity<String> registerAgency(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>("Username already exists!", HttpStatus.BAD_REQUEST);
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            return new ResponseEntity<>("Email already exists!", HttpStatus.BAD_REQUEST);
        }

        userService.registerAgency(user);
        return new ResponseEntity<>("Agency registered successfully!", HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<String> login() {
        // Handle login logic here
        return new ResponseEntity<>("Login logic goes here!", HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        // Handle logout logic here
        return new ResponseEntity<>("Logout logic goes here!", HttpStatus.OK);
    }
}
