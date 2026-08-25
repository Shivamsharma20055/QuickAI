package com.learning.QuickAI.service;

import com.learning.QuickAI.model.UserGenerations;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


@Service
public class AIService {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private BlogTitleService blogService;
    @Autowired
    private ImageGenerateService imageService;
    public UserGenerations generateArticle(String prompt,String length){
        return articleService.generateArticle(prompt,length);

    }
    public UserGenerations generateBlogTitles(String prompt,String category){
        return blogService.generateBlogTitles(prompt,category);
    }
    public UserGenerations generateImage(String prompt,String style){
        return imageService.generateImage(prompt,style);
    }
}
