package com.inshorts.newssense.ai.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "news_articles")
public class NewsArticle {
    @Id
    private UUID id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String url;

    private LocalDateTime publicationDate;

    private String sourceName;

    @ElementCollection
    @CollectionTable(name = "news_article_category", joinColumns = @JoinColumn(name = "article_id"))
    @Column(name = "category")
    private List<String> category;

    private float relevanceScore;

    private double latitude;

    private double longitude;

    @Column(columnDefinition = "TEXT")
    private String llmSummary;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public float getRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(float relevanceScore) {
        this.relevanceScore = relevanceScore;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLlmSummary() {
        return llmSummary;
    }

    public void setLlmSummary(String llmSummary) {
        this.llmSummary = llmSummary;
    }
}
