package com.learning.QuickAI.service;

import com.learning.QuickAI.model.UserGenerations;
import com.learning.QuickAI.repo.UserGenerationsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;

@Service
public class BlogTitleService {
    @Autowired
    private UserGenerationsRepo generationsRepo;
    @Autowired
     private APIKeyService apiService;
    RestClient client=RestClient.create();
    private ObjectMapper mapper=new ObjectMapper();
    public UserGenerations generateBlogTitles(String prompt,String category){
        String keys[]=apiService.getAllKeys();

        String body=   """
                {
                 "contents": [
                  {
                     "parts":  [
                        {
                            "text": "Generate some creative, engaging, and SEO-friendly blog titles. Keyword is %s and Category is %s. Keep them unique, concise, relevant, and non-clickbait."
                         }
       
                     ]
                  }
                  ]
                 }
                """.formatted(prompt,category);
        String response="";
        for(String key:keys) {
            try {
                 response = client
                        .post()
                        .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent?key=" + key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(body)
                        .retrieve()
                        .body(String.class);
            }
            catch(HttpClientErrorException.TooManyRequests e){
                System.out.println("gemini limit exceeded");
            }

        }
        JsonNode json= mapper.readTree(response);
        String answer= json
                .get("candidates")
                .get(0)
                .get("content")
                .get("parts")
                .get(0)
                .get("text")
                .asText();
        UserGenerations generate=new UserGenerations();
        generate.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        generate.setPrompt(prompt);
        generate.setContent(answer);
        generate.setPublish(false);
        generate.setType("blog");
        generate.setLikes(new ArrayList<>());
        return generationsRepo.save(generate);




    }
}
