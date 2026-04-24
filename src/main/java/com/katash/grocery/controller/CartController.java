package com.katash.grocery.controller;

import com.katash.grocery.filter.JWTFilter;
import com.katash.grocery.model.Users;
import com.katash.grocery.repository.UserRepo;
import com.katash.grocery.service.CartService;
import com.katash.grocery.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    JWTService jwtService;

    @Autowired
    UserRepo userRepo;

    private Users getUserIdFromToken(String token){
        String email = jwtService.getUsernameFromToken(token.substring(7));
        return userRepo.findByEmail(email);
    }


    @PostMapping("/add/{productId}")
    public ResponseEntity<Object> addToCart(@RequestHeader("Authorization") String token,
                                            @PathVariable int productId,
                                            @RequestParam int quantity){

//        String email = jwtService.getUsernameFromToken(token.substring(7));
//        Users user = userRepo.findByEmail(email);
        Users user = getUserIdFromToken(token);
        return cartService.addToCart(user, productId, quantity);
    }

    @GetMapping("/view")
    public ResponseEntity<Object> getUserCart(@RequestHeader("Authorization") String token){
        int id = getUserIdFromToken(token).getUserId();
        return cartService.getUserCart(id);
    }
}
