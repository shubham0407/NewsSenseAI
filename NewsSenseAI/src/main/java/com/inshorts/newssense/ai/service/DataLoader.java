package com.inshorts.newssense.ai.service;

import com.inshorts.newssense.ai.dto.NewsArticleDto;
import com.inshorts.newssense.ai.model.NewsArticle;
import com.inshorts.newssense.ai.repository.NewsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DataLoader {
    private static final long DELAY_MS = 2000;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private LLMService llmService;

    public void loadData(List<NewsArticleDto> dtos) {
        log.info("Starting sequential loading of {} articles...", dtos.size());

        int limit = Math.min(10000, dtos.size());
        for (int i = 0; i < limit; i++) {
            NewsArticleDto dto = dtos.get(i);
            processAndSave(dto);
            try {
                Thread.sleep(DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Interrupted during delay after processing article {}", dto.getId(), e);
            }
        }
        log.info("Finished processing all articles.");
    }

    private void processAndSave(NewsArticleDto dto) {
        try {
            NewsArticle article = buildArticle(dto);
            article.setLlmSummary(llmService.summarize(dto.getDescription()));
            newsRepository.save(article);
            log.info("Saved article::::::: {}", dto.getId());
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





