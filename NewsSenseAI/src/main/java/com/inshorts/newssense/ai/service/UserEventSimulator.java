package com.inshorts.newssense.ai.service;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserEventSimulator {

    private final Map<UUID, ArticleStats> statsMap = new ConcurrentHashMap<>();

    @Scheduled(fixedRate = 10000) // every 10 seconds
    public void simulateEvents() {
        Random rand = new Random();
        UUID articleId = UUID.randomUUID(); // Replace with existing UUIDs in real case
        ArticleStats stats = statsMap.getOrDefault(articleId, new ArticleStats());
        stats.setViews(stats.getViews() + rand.nextInt(5));
        stats.setLikes(stats.getLikes() + rand.nextInt(2));
        stats.setShares(stats.getShares() + rand.nextInt(1));
        statsMap.put(articleId, stats);
        System.out.println("Updated stats for article " + articleId + ": " + stats);
    }

    public Map<UUID, ArticleStats> getStatsMap() {
        return statsMap;
    }

    public static class ArticleStats {
        private int views;
        private int likes;
        private int shares;

        public ArticleStats() {
            this.views = 0;
            this.likes = 0;
            this.shares = 0;
        }

        public ArticleStats(int views, int likes, int shares) {
            this.views = views;
            this.likes = likes;
            this.shares = shares;
        }

        public int getViews() {
            return views;
        }

        public void setViews(int views) {
            this.views = views;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public int getShares() {
            return shares;
        }

        public void setShares(int shares) {
            this.shares = shares;
        }

        @Override
        public String toString() {
            return "ArticleStats{" +
                    "views=" + views +
                    ", likes=" + likes +
                    ", shares=" + shares +
                    '}';
        }
    }

}
