package com.learning.QuickAI.service;

import com.learning.QuickAI.model.Users;
import com.learning.QuickAI.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {
    @Autowired
    private UsersRepo userRepo;
    public Users registerUser(Users user) {
        return userRepo.save(user);

    }
}
