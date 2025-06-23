package com.inshorts.newssense.ai.service;

import com.inshorts.newssense.ai.dto.NewsArticleDto;
import com.inshorts.newssense.ai.model.NewsArticle;
import com.inshorts.newssense.ai.repository.NewsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataLoader {

    private static final int BATCH_SIZE = 20; // adjust as needed
    private static final long BATCH_DELAY_MS = 10_000L; // 10-second delay between batches

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private LLMService llmService;

    public void loadData(List<NewsArticleDto> dtos) {
        log.info("Loading data... ");
        // Split into batches
        List<List<NewsArticleDto>> batches = new ArrayList<>();
        for (int i = 0; i < dtos.size(); i += BATCH_SIZE) {
            batches.add(dtos.subList(i, Math.min(i + BATCH_SIZE, dtos.size())));
        }

        for (List<NewsArticleDto> batch : batches) {
            // Create Callable tasks for the batch
            List<Callable<Void>> tasks = new ArrayList<>();
            for (final NewsArticleDto dto : batch) {
                tasks.add(new Callable<Void>() {
                    @Override
                    public Void call() {
                        processAndSave(dto);
                        return null;
                    }
                });
            }

            // Execute the batch
            try {
                executor.invokeAll(tasks);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Batch interrupted", e);
            }

            // Optional pause between batches
            try {
                Thread.sleep(BATCH_DELAY_MS);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                log.warn("Batch delay interrupted", ie);
            }
        }
    }

    private void processAndSave(NewsArticleDto dto) {
        try {
            NewsArticle article = buildArticle(dto);
            article.setLlmSummary(llmService.summarize(dto.getDescription()));
            newsRepository.save(article);
            log.info("Saved article {}", dto.getId());
        } catch (Exception ex) {
            log.error("Failed processing article {}", dto.getId(), ex);
        }
    }

    private NewsArticle buildArticle(NewsArticleDto dto) {
        NewsArticle article = new NewsArticle();
        article.setId(UUID.fromString(dto.getId()));
        article.setTitle(dto.getTitle());
        article.setDescription(dto.getDescription());
        article.setUrl(dto.getUrl());
        article.setPublicationDate(LocalDateTime.parse(dto.getPublication_date()));
        article.setSourceName(dto.getSource_name());
        article.setCategory(dto.getCategory());
        article.setRelevanceScore(dto.getRelevance_score());
        article.setLatitude(dto.getLatitude());
        article.setLongitude(dto.getLongitude());
        return article;
    }
}





