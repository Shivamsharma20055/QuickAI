package com.learning.QuickAI.controller;

import com.learning.QuickAI.model.Users;
import com.learning.QuickAI.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAuthController {
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private AuthenticationManager authManager;
    @PostMapping("/register")
    public Users registerUser(@RequestBody Users user){
        return userAuthService.registerUser(user);


    }
    @PostMapping("/login")
    public String loginUser(@RequestBody Users user){
        Authentication authentication =authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        if(authentication.isAuthenticated()){
            return "login succeed";

        }
        return "failed to login";

    }
}
