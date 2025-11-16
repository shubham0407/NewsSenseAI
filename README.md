# Contextual News Data Retrieval System

This project implements a backend system that processes user queries using Large Language Models (LLMs), retrieves news articles from a database, ranks them using multiple strategies, and returns enriched JSON responses. It simulates real-world news API functionalities, including category filters, text search, proximity search, score-based filtering, source-based filtering, and trending news by location.

## 🚀 Overview

This project demonstrates an intelligent news-fetching backend with the ability to:

- Understand user queries (including location awareness).

- Use an LLM to extract entities, concepts, and intent.

- Select different retrieval modes (simulated API endpoints).

- Load news data from JSON files into a database.

- Fetch, filter, rank, and enrich articles.

- Provide a unified JSON output format.

- (Bonus) Provide location-based trending news using simulated user events.

## 🧠 LLM Query Processing

- The system uses a publicly available LLM API (OpenAI, Google, etc.) to:

- Identify entities such as people, organizations, locations, and events.

- Understand the user’s intent to determine the appropriate retrieval method:

  - category

  - score

  - search

  - source

  - nearby

### Example:
#### Query: 
          "Latest developments in the Elon Musk Twitter acquisition near Palo Alto"
### LLM Output:

#### Entities: 
              Elon Musk, Twitter, Palo Alto

#### Intent: 
              nearby

## 🗄️ Data Source and Storage

News articles are provided in JSON files.

These files are loaded into a database (SQL or NoSQL).

#### Each article includes fields such as:

```json
{
  "id": "b1793e11-85f1-47f4-b836-ddc21dd8991e",
  "title": "Paris police chase over running of red light ends in pileup | DW News",
  "description": "Paris police chase over running of red light ends in pileup | DW News...",
  "url": "https://www.youtube.com/@dwnews",
  "publication_date": "2025-03-24T11:08:11",
  "source_name": "DW",
  "category": ["General"],
  "relevance_score": 0.51,
  "latitude": 21.754075,
  "longitude": 80.560129
}
```

## 🔌 API Endpoints (Simulated)

All endpoints are typically exposed under:

/api/v1/news/

1. Category-Based Retrieval

Retrieve articles by category.
Ranking: newest publication date first.

2. Score-Based Retrieval

Retrieve articles with relevance scores above a threshold.
Ranking: highest relevance first.

3. Text Search

Search within article titles and descriptions.
Ranking: combined text-match and relevance score.

4. Source-Based Retrieval

Retrieve articles from a specific news source.
Ranking: newest publication date first.

5. Proximity-Based Retrieval

Retrieve articles near a given location using latitude & longitude.
Ranking: shortest distance first (Haversine formula).

/api/v1/news/nearby?lat=37.42&lon=-122.08&radius=10

## 🔥 Bonus: Location-Based Trending News
### Endpoint:
GET /api/v1/news/trending?lat=&lon=&limit=


#### Features:

- Simulated user events (views, clicks, etc.)

- Computes a trending score based on:

- Volume and type of interactions

- Recency

- Proximity to user

- Uses caching based on geospatial segmentation to improve performance.

## 📊 Ranking Logic
Retrieval Mode	Ranking Method
Category	Newest publication date
Source	Newest publication date
Score	Highest relevance score
Search	Text match score + relevance score
Nearby	Minimum distance
Trending	Trending score

## 🧩 LLM-Based Enrichment

#### Each returned article is enriched with an LLM-generated summary:

     "llm_summary": "This article discusses the latest developments in..."


### 📦 JSON Response Format

The top 5 most relevant articles are returned with a consistent JSON structure:

```json
{
  "articles": [
    {
      "title": "Example Title",
      "description": "Example description...",
      "url": "https://example.com/article1",
      "publication_date": "2024-04-28T10:00:00Z",
      "source_name": "Example News",
      "category": ["Technology"],
      "relevance_score": 0.92,
      "llm_summary": "This article covers...",
      "latitude": 37.4220,
      "longitude": -122.0840
    }
  ]
}
```

## ❗ Error Handling

- Uses appropriate HTTP status codes

- Returns descriptive error messages

- Includes validation for missing parameters

