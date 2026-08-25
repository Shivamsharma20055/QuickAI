package com.learning.QuickAI.controller;

import com.learning.QuickAI.model.UserGenerations;
import com.learning.QuickAI.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.Map;

@RestController
public class AIController {
    @Autowired
    private AIService aiService;
    @Value("${gemini.api.key}")
    private String apiKey;

    @GetMapping("/")
    public String greet(){

        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    @PostMapping("/article")
    public UserGenerations generateArticle(@RequestBody Map<String,Object> request){
        String prompt=(String) request.get("prompt");
        String length=(String) request.get("length");
        return aiService.generateArticle(prompt,length);
    }
    @PostMapping("/blog")
    public UserGenerations generateBlogTitle(@RequestBody Map<String,Object>request){
        String prompt=(String)request.get("prompt");
        String category=(String)request.get("category");
        return aiService.generateBlogTitles(prompt,category);
    }
    @PostMapping("/image")
    public UserGenerations generateImage(@RequestBody Map<String,Object>request){
        String prompt=(String) request.get("prompt");
        String style=(String)request.get("style");
        return aiService.generateImage(prompt,style);

    }

}
