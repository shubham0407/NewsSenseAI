package com.inshorts.newssense.ai.controller;


import com.inshorts.newssense.ai.model.NewsArticle;
import com.inshorts.newssense.ai.service.LLMService;
import com.inshorts.newssense.ai.service.TrendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/trending")
public class TrendingController {

    private final TrendingService trendingService;
    private final LLMService llmService;


    @Autowired
    public TrendingController(TrendingService trendingService, LLMService llmService) {
        this.trendingService = trendingService;
        this.llmService = llmService;
    }

//    @GetMapping
//    public List<Map<String, String>> getTrendingWithSummary() {
//        List<NewsArticle> trendingArticles = trendingService.getTrendingArticles();
//
//        List<Map<String, String>> response = new ArrayList<>();
//        for (NewsArticle article : trendingArticles) {
//            Map<String, String> map = new HashMap<>();
//            map.put("title", article.getTitle());
//            map.put("summary", llmService.enrichSummary(article.getDescription())); // corrected here
//            response.add(map);
//        }
//        return response;
//    }
}
