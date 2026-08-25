package com.learning.QuickAI.service;

import com.learning.QuickAI.model.Users;
import com.learning.QuickAI.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepo repo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user=repo.getUserByUsername(username);
        if(user==null){
            System.out.println("no user");
            throw new UsernameNotFoundException("no user found");
        }
        UserDetails userDetails= User
                .withUsername(user.getUsername())
                .roles("USER")
                .password(user.getPassword())
                .authorities(new SimpleGrantedAuthority("USER"))
                .build();
        return userDetails;


    }
}
