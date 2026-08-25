package com.learning.QuickAI.service;

import com.learning.QuickAI.model.UserGenerations;
import com.learning.QuickAI.repo.UserGenerationsRepo;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;

@Service
public class ImageGenerateService {
    @Value("${hugging.api.key}")
    private String apiKey;
    @Autowired
    private APIKeyService apiService;
    @Autowired
    private UserGenerationsRepo generationsRepo;
    private RestClient client= RestClient.create();
    private ObjectMapper mapper=new ObjectMapper();
    public UserGenerations generateImage(String prompt,String style){
        //String keys[]=apiService.getAllKeys();
        String body = """
                {
                    "prompt": "Generate the Image like %s and style should be like %s"
                }
                """.formatted(prompt,style);
        String response = client
                        .post()
                          .uri("https://router.huggingface.co/fal-ai/fal-ai/krea-2/turbo")                        .header("Authorization", "Bearer "+apiKey)
                        .header("Content-Type", "application/json")
                         .header("accept","image/*")
                         .body(body)
                         .retrieve()
                        .body(String.class);
        JsonNode root=mapper.readTree(response);
        String url=root
                .get("images")
                .get(0)
                .get("url")
                .asText();
        UserGenerations generations=new UserGenerations();
        generations.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        generations.setLikes(new ArrayList<>());
        generations.setType("image");
        generations.setPrompt(prompt);
        generations.setPublish(true);
        generations.setContent(url);
        generationsRepo.save(generations);



        return generations;
    }
}
