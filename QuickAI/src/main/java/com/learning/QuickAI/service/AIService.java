package com.learning.QuickAI.service;

import com.learning.QuickAI.model.UserGenerations;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public class AIService {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private BlogTitleService blogService;
    @Autowired
    private ImageGenerateService imageService;
    @Autowired
    private ResumeReviewService resumeService;
    public UserGenerations generateArticle(String prompt,String length){
        return articleService.generateArticle(prompt,length);

    }
    public UserGenerations generateBlogTitles(String prompt,String category){
        return blogService.generateBlogTitles(prompt,category);
    }
    public UserGenerations generateImage(String prompt,String style){
        return imageService.generateImage(prompt,style);
    }

    public UserGenerations reviewResume(MultipartFile resume) throws IOException {
        return resumeService.reviewResume(resume);
    }
}
