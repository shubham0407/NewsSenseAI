package com.inshorts.newssense.ai.service;

import com.inshorts.newssense.ai.model.NewsArticle;
import com.inshorts.newssense.ai.repository.NewsRepository;
import com.inshorts.newssense.ai.util.TrendingScoreCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrendingService {

    private final UserEventSimulator userEventSimulator;
    private final NewsRepository newsRepository;

    @Autowired
    public TrendingService(UserEventSimulator userEventSimulator, NewsRepository newsRepository) {
        this.userEventSimulator = userEventSimulator;
        this.newsRepository = newsRepository;
    }

    public List<NewsArticle> getTrendingArticles() {
        Map<UUID, UserEventSimulator.ArticleStats> statsMap = userEventSimulator.getStatsMap();
        return statsMap.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(
                        TrendingScoreCalculator.calculateTrendingScore(
                                e2.getValue().getViews(), e2.getValue().getLikes(), e2.getValue().getShares()),
                        TrendingScoreCalculator.calculateTrendingScore(
                                e1.getValue().getViews(), e1.getValue().getLikes(), e1.getValue().getShares())
                ))
                .limit(5)
                .map(entry -> newsRepository.findById(entry.getKey()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


}
