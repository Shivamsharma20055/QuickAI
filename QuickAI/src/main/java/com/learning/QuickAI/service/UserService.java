package com.learning.QuickAI.service;

import com.learning.QuickAI.model.UserGenerations;
import com.learning.QuickAI.model.Users;
import com.learning.QuickAI.repo.UserGenerationsRepo;
import com.learning.QuickAI.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserGenerationsRepo userGenerationsRepo;
    @Transactional(readOnly = true)
    public List<UserGenerations> getUserCreations(String username) {
        return userGenerationsRepo.getCreationsByUsername(username);

    }

    public List<UserGenerations> getPublishedCreations(String username) {
        return userGenerationsRepo.getPublishedCreations(username);
    }

    public UserGenerations toggleLike(Integer id) {
       UserGenerations generations=  userGenerationsRepo.findById(id).orElse(null);
       String username=SecurityContextHolder.getContext().getAuthentication().getName();
       List<String>likes=generations.getLikes();
       if(likes.contains(username))likes.remove(username);
       else likes.add(username);
       generations.setLikes(likes);
       return userGenerationsRepo.save(generations);
    }
}
