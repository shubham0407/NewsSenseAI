package com.inshorts.newssense.ai.service;

import com.inshorts.newssense.ai.model.NewsArticle;
import com.inshorts.newssense.ai.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public Page<NewsArticle> getByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findByCategoryContainingIgnoreCaseOrderByPublicationDateDesc(category, pageable);
    }

    public Page<NewsArticle> getBySource(String source, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findBySourceNameIgnoreCaseOrderByPublicationDateDesc(source, pageable);
    }

    public Page<NewsArticle> getByScore(float threshold, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findByRelevanceScoreGreaterThanOrderByRelevanceScoreDesc(threshold, pageable);
    }

    public Page<NewsArticle> getBySearch(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.searchByTitleOrDescription(query, pageable);
    }

    public NewsArticle getById(UUID id) {
        Optional<NewsArticle> newsArticle = newsRepository.findById(id);//.orElseThrow(() -> new EntityNotFoundException("NewsArticle not found with id " + id)));
        if (!newsArticle.isPresent()) {
            throw new EntityNotFoundException("NewsArticle not found with id " + id);
        }
        return newsArticle.get();
    }


    public Page<NewsArticle> getNearby(double lat, double lon, double radiusKm, int page, int size) {
        List<NewsArticle> filtered = newsRepository.findAll().stream().filter(article -> {
            double distance = haversine(lat, lon, article.getLatitude(), article.getLongitude());
            return distance <= radiusKm;
        }).sorted(Comparator.comparingDouble(article -> haversine(lat, lon, article.getLatitude(), article.getLongitude()))).collect(Collectors.toList());
        int start = page * size;
        int end = Math.min(start + size, filtered.size());
        List<NewsArticle> paged = (start <= end) ? filtered.subList(start, end) : Collections.emptyList();
        return new org.springframework.data.domain.PageImpl<>(paged, PageRequest.of(page, size), filtered.size());
    }

    @Cacheable(value = "trendingNews", key = "#lat + '-' + #lon")
    public List<NewsArticle> getTrendingNews(double lat, double lon, int limit) {
        List<NewsArticle> allArticles = newsRepository.findAll();
        return allArticles.stream().sorted(Comparator.comparingDouble(a -> -trendingScore(a, lat, lon))).limit(limit).collect(Collectors.toList());
    }

    private double trendingScore(NewsArticle article, double lat, double lon) {
        double distance = haversine(lat, lon, article.getLatitude(), article.getLongitude());
        double proximityScore = 1.0 / (1.0 + distance);
        double freshness = 1.0 / (1.0 + Math.abs(article.getPublicationDate().until(java.time.LocalDateTime.now(), java.time.temporal.ChronoUnit.HOURS)));
        double interactionWeight = Math.random();
        return proximityScore * 0.4 + freshness * 0.4 + interactionWeight * 0.2;
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}