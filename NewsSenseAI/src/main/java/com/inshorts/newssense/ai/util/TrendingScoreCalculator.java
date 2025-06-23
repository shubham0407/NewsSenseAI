package com.inshorts.newssense.ai.util;

public class TrendingScoreCalculator {
    public static double calculateTrendingScore(int views, int likes, int shares) {
        return views * 0.5 + likes * 1.0 + shares * 2.0;
    }
}