package com.katash.grocery.service;

import com.katash.grocery.model.Users;
import com.katash.grocery.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepo repo;

    @Autowired
    JWTService jwtService;

    public Users register(Users user) {
        user.setRole("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());
        return repo.save(user);
    }

    private String getRole(Authentication authentication){
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("NO_ROLE");
    }

    public ResponseEntity<Object> verifyUser(Users user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));

        String role = getRole(authentication);

        if(authentication.isAuthenticated() && role.equals("ROLE_USER")){
            return new ResponseEntity<>(jwtService.generateToken(user.getEmail(),role), HttpStatus.OK);
        }
        return new ResponseEntity<>("Admin login not allowed.",HttpStatus.UNAUTHORIZED);
    }


    public ResponseEntity<Object> verifyAdmin(Users user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));

        String role = getRole(authentication);

        if(authentication.isAuthenticated() && role.equals("ROLE_ADMIN")){
            return new ResponseEntity<>(jwtService.generateToken(user.getEmail(),role), HttpStatus.OK);
        }
        return new ResponseEntity<>("User login not allowed.",HttpStatus.UNAUTHORIZED);
    }
}
