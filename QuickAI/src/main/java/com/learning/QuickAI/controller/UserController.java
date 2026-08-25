package com.learning.QuickAI.controller;

import com.learning.QuickAI.model.UserGenerations;
import com.learning.QuickAI.model.Users;
import com.learning.QuickAI.service.UserAuthService;
import com.learning.QuickAI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/creations")
    public List<UserGenerations> getUserCreations(){
        return userService.getUserCreations(SecurityContextHolder.getContext().getAuthentication().getName());

    }
    @GetMapping("/publishedCreations")
    public List<UserGenerations> getPublishedCreations(){
        return userService.getPublishedCreations(SecurityContextHolder.getContext().getAuthentication().getName());

    }
    @GetMapping("/toggleLike/{id}")
    public UserGenerations toggleLike(@PathVariable int id){
        return userService.toggleLike(id);

    }
}
