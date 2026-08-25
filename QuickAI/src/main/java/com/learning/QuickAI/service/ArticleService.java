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
public class ArticleService {
    @Autowired
    private UserGenerationsRepo generationsRepo;
    @Autowired
    private APIKeyService apiService;
    private RestClient rest=RestClient.create();
    private ObjectMapper mapper=new ObjectMapper();
    public UserGenerations generateArticle(String prompt,String length){
        String keys[]=apiService.getAllKeys();
        String body=   """
                {
                 "contents": [
                  {
                     "parts":  [
                        {
                            "text": "%s.Generate the article in %s word"
                         }
       
                     ]
                  }
                  ]
                 }
                """.formatted(prompt,length);
        String response="";
        for(String key:keys) {
            try {
                 response = rest
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
        System.out.println(response);
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
        generate.setContent(answer);
        generate.setPrompt(prompt);
        generate.setType("Article");
        generate.setPublish(false);
        generate.setLikes(new ArrayList<>());
        return generationsRepo.save(generate);
    }
}
