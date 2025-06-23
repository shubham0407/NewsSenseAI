package com.inshorts.newssense.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
public class NewsArticleResponseDto {

    private UUID id;
    private String title;
    private String description;
    private String source;
    private List<String> category;
    private double latitude;
    private double longitude;
    private float relevanceScore;
    private LocalDateTime publishedAt;
//    private List<String> tags;
}
