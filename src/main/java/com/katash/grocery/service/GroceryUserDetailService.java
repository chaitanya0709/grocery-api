package com.katash.grocery.service;

import com.katash.grocery.model.UserPrinciple;
import com.katash.grocery.model.Users;
import com.katash.grocery.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GroceryUserDetailService implements UserDetailsService {
    @Autowired
    UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = repo.findByEmail(email);
        if (user==null){
            throw new UsernameNotFoundException("User not found");
        } else {
            return new UserPrinciple(user);
        }
    }
}
