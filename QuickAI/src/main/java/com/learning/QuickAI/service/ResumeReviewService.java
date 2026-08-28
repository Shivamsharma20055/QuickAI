package com.learning.QuickAI.service;

import com.learning.QuickAI.model.UserGenerations;
import com.learning.QuickAI.repo.UserGenerationsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

@Service
public class ResumeReviewService {
    @Autowired
    private APIKeyService apiService;
    @Autowired
    private UserGenerationsRepo generationsRepo;
    private RestClient client= RestClient.create();
    private ObjectMapper mapper=new ObjectMapper();
    public UserGenerations reviewResume(MultipartFile resume) throws IOException {
        String keys[]=apiService.getAllKeys();
        String name=resume.getOriginalFilename();
        byte bytes[]=resume.getBytes();
        Path uploadDir=Paths.get("uploads/");
        Files.createDirectories(uploadDir);
        Path path= uploadDir.resolve( name);
        Files.write(path,bytes);
        String resumeBytes=Base64.getEncoder().encodeToString(bytes);

        String body = """
{
  "contents": [
    {
      "parts": [
        {
          "text": "Review this resume and provide an overall score, ATS score, strengths, weaknesses, skills, and suggestions for improvement."
        },
        {
          "inline_data": {
            "mime_type": "application/pdf",
            "data": "%s"
          }
        }
      ]
    }
  ]
}
""".formatted(resumeBytes);
        String response="";
        for(String key:keys){
            try {
                response = client
                        .post()
                        .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent")
                        .header("content-type", "application/json")
                        .header("x-goog-api-key", key)
                        .body(body)
                        .retrieve()
                        .body(String.class);
            }
            catch(HttpClientErrorException.TooManyRequests e){
                System.out.println("gemini limit exceeded");
            }


        }
        JsonNode root=mapper.readTree(response);
        String answer=root
                .get("candidates")
                .get(0)
                .get("content")
                .get("parts")
                .get(0)
                .get("text")
                .asText();
        UserGenerations generations= new UserGenerations();
        generations.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        generations.setContent(answer);
        generations.setPublish(true);
        generations.setPrompt("review resume");
        generations.setLikes(new ArrayList<>());
        generations.setType("text");
        generationsRepo.save(generations);



        return generations;
    }
}
