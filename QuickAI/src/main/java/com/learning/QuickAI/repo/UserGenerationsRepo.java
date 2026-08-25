package com.learning.QuickAI.repo;

import com.learning.QuickAI.model.UserGenerations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGenerationsRepo extends JpaRepository<UserGenerations,Integer> {
    @Query("select u from UserGenerations u where u.username=:username")
    List<UserGenerations> getCreationsByUsername(String username);
    @Query("select u from UserGenerations u where u.username=:username and u.publish=true")
    List<UserGenerations> getPublishedCreations(String username);
}
