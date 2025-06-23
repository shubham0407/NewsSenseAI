package com.inshorts.newssense.ai.repository;

import com.inshorts.newssense.ai.model.NewsArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NewsRepository extends JpaRepository<NewsArticle, UUID> {


    Page<NewsArticle> findByCategoryContainingIgnoreCaseOrderByPublicationDateDesc(String category, Pageable pageable);

    Page<NewsArticle> findBySourceNameIgnoreCaseOrderByPublicationDateDesc(String sourceName, Pageable pageable);

    Page<NewsArticle> findByRelevanceScoreGreaterThanOrderByRelevanceScoreDesc(float threshold, Pageable pageable);

    @Query("SELECT n FROM NewsArticle n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(n.description) LIKE LOWER(CONCAT('%', :query, '%')) ORDER BY n.publicationDate DESC")
    Page<NewsArticle> searchByTitleOrDescription(String query, Pageable pageable);


//    Page<NewsArticle> findByContentContainingIgnoreCaseAndLocationIgnoreCase(String content, String location, Pageable pageable);


}
