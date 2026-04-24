package com.katash.grocery.controller;

import com.katash.grocery.model.Users;
import com.katash.grocery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService service;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @PostMapping("user/register")
    public ResponseEntity<Object> register(@RequestBody Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            return new ResponseEntity<>(service.register(user), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException ex) { // Example: Unique constraint violation
            return new ResponseEntity<>("User already exists or data integrity violation.", HttpStatus.CONFLICT);
        } catch (IllegalArgumentException ex) { // Example: Invalid arguments passed
            return new ResponseEntity<>("Invalid input provided.", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) { // Catch-all for unexpected exceptions
            return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("user/login")
    public ResponseEntity<Object> verifyUser(@RequestBody Users user){
        return service.verifyUser(user);
    }

    @PostMapping("admin/login")
    public ResponseEntity<Object> verifyAdmin(@RequestBody Users user){
        return service.verifyAdmin(user);
    }
}
