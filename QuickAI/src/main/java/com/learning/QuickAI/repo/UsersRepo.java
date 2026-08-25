package com.learning.QuickAI.repo;

import com.learning.QuickAI.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepo extends JpaRepository<Users,Integer> {
    @Query("select u from Users u where u.username = :username")
    Users getUserByUsername(String username);
}
