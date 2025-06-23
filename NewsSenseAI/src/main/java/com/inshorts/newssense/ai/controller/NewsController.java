package com.inshorts.newssense.ai.controller;

import com.inshorts.newssense.ai.dto.NewsArticleDto;
import com.inshorts.newssense.ai.model.NewsArticle;
import com.inshorts.newssense.ai.service.DataLoader;
import com.inshorts.newssense.ai.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

    private final NewsService newsService;
    private  final DataLoader dataLoader;

    @Autowired
    public NewsController(NewsService newsService, DataLoader dataLoader) {
        this.newsService = newsService;
        this.dataLoader = dataLoader;
    }



    @GetMapping("/category")
    public ResponseEntity<Page<NewsArticle>> getByCategory(
            @RequestParam String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(newsService.getByCategory(category, page, size));
    }

    @GetMapping("/source")
    public ResponseEntity<Page<NewsArticle>> getBySource(
            @RequestParam String source,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(newsService.getBySource(source, page, size));
    }

    @GetMapping("/score")
    public ResponseEntity<Page<NewsArticle>> getByScore(
            @RequestParam float threshold,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(newsService.getByScore(threshold, page, size));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<NewsArticle>> getBySearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(newsService.getBySearch(query, page, size));
    }

    @GetMapping("/nearby")
    public ResponseEntity<Page<NewsArticle>> getNearby(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam double radius,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(newsService.getNearby(lat, lon, radius, page, size));
    }

    @GetMapping("/trending")
    public ResponseEntity<List<NewsArticle>> getTrending(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(newsService.getTrendingNews(lat, lon, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsArticle> getNewsArticleById(@PathVariable UUID id){
        return ResponseEntity.ok(newsService.getById(id));
    }

    @PostMapping("/load/news")
    public ResponseEntity<Void> loadNewsData(@RequestBody List<NewsArticleDto> newsArticleDtos) {
        dataLoader.loadData(newsArticleDtos);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
