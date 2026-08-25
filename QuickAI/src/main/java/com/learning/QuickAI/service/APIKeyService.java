package com.learning.QuickAI.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class APIKeyService {
    @Value("${gemini.api.key}")
    private String apiKey;
    @Value("${gemini.api.key2}")
    private String apiKey2;
    @Value("${gemini.api.key3}")
    private String apiKey3;
    @Value("${gemini.api.key4}")
    private String apiKey4;
    public String [] getAllKeys(){
        String keys[]={apiKey,apiKey2,apiKey3,apiKey4};
        return keys;
    }
}
