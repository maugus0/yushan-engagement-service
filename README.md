# Yushan Engagement Service

> 💬 **Engagement Service for Yushan Webnovel Platform.** - Manages user interactions including comments, reviews, ratings, likes, bookmarks, and social features to foster community engagement.

# Yushan Platform - Engagement Service Setup Guide

## Architecture Overview

```
┌─────────────────────────────┐
│   Eureka Service Registry   │
│       localhost:8761        │
└──────────────┬──────────────┘
               │
┌──────────────┴──────────────┐
│   Service Registration &     │
│      Discovery Layer         │
└──────────────┬──────────────┘
               │
    ┌──────────┴──────────┬───────────────┬──────────┬──────────┐
    │                     │               │          │          │
    ▼                     ▼               ▼          ▼          ▼
┌────────┐          ┌─────────┐  ┌────────────┐ ┌──────────┐ ┌──────────┐
│  User  │          │ Content │  │ Engagement │ │Gamifica- │ │Analytics │
│Service │◄────────►│ Service │◄─┤  Service   ├─►│  tion    │ │ Service  │
│ :8081  │          │  :8082  │  │   :8084    ├─►│ Service  │ │  :8083   │
└────────┘          └─────────┘  └─────┬──────┘ │  :8085   │ └──────────┘
    │                     │             │        └──────────┘      │
    └─────────────────────┴─────────────┴──────────────────────────┘
                    Inter-service Communication
                      (via Feign Clients)
                              │
                    ┌─────────┴─────────┐
                    │   Social Layer     │
                    │   Comments, Likes  │
                    │   Reviews, Follows │
                    └───────────────────┘
```

---
## Prerequisites

Before setting up the Engagement Service, ensure you have:
1. **Java 21** installed
2. **Maven 3.8+** or use the included Maven wrapper
3. **Eureka Service Registry** running
4. **PostgreSQL 15+** (for engagement data storage)
5. **Redis** (for caching and real-time notifications)
6. **Elasticsearch** (optional, for advanced comment/review search)

---
## Step 1: Start Eureka Service Registry

**IMPORTANT**: The Eureka Service Registry must be running before starting any microservice.

```bash
# Clone the service registry repository
git clone https://github.com/maugus0/yushan-platform-service-registry
cd yushan-platform-service-registry

# Option 1: Run with Docker (Recommended)
docker-compose up -d

# Option 2: Run locally
./mvnw spring-boot:run
```

### Verify Eureka is Running

- Open: http://localhost:8761
- You should see the Eureka dashboard

---

## Step 2: Clone the Engagement Service Repository

```bash
git clone https://github.com/maugus0/yushan-engagement-service.git
cd yushan-engagement-service

# Option 1: Run with Docker (Recommended)
docker-compose up -d

# Option 2: Run locally (requires PostgreSQL 15 and Redis to be running beforehand)
./mvnw spring-boot:run
```

---

## Expected Output

### Console Logs (Success)

```
2024-10-16 10:30:15 - Starting EngagementServiceApplication
2024-10-16 10:30:18 - Tomcat started on port(s): 8084 (http)
2024-10-16 10:30:20 - DiscoveryClient_ENGAGEMENT-SERVICE/engagement-service:8084 - registration status: 204
2024-10-16 10:30:20 - Started EngagementServiceApplication in 9.1 seconds
```

### Eureka Dashboard

```
Instances currently registered with Eureka:
✅ ENGAGEMENT-SERVICE - 1 instance(s)
   Instance ID: engagement-service:8084
   Status: UP (1)
```

---

## API Endpoints

### Health Check
- **GET** `/api/v1/health` - Service health status

### Comments
- **POST** `/api/v1/comments` - Create a comment
- **GET** `/api/v1/comments/{commentId}` - Get comment details
- **PUT** `/api/v1/comments/{commentId}` - Update comment
- **DELETE** `/api/v1/comments/{commentId}` - Delete comment
- **GET** `/api/v1/comments/novels/{novelId}` - Get novel comments
- **GET** `/api/v1/comments/chapters/{chapterId}` - Get chapter comments
- **POST** `/api/v1/comments/{commentId}/reply` - Reply to comment
- **GET** `/api/v1/comments/{commentId}/replies` - Get comment replies

### Reviews
- **POST** `/api/v1/reviews` - Create a review
- **GET** `/api/v1/reviews/{reviewId}` - Get review details
- **PUT** `/api/v1/reviews/{reviewId}` - Update review
- **DELETE** `/api/v1/reviews/{reviewId}` - Delete review
- **GET** `/api/v1/reviews/novels/{novelId}` - Get novel reviews
- **GET** `/api/v1/reviews/users/{userId}` - Get user's reviews

### Ratings
- **POST** `/api/v1/ratings` - Rate content (novel/chapter)
- **GET** `/api/v1/ratings/novels/{novelId}` - Get novel rating summary
- **GET** `/api/v1/ratings/users/{userId}/novel/{novelId}` - Get user's rating
- **PUT** `/api/v1/ratings/{ratingId}` - Update rating
- **DELETE** `/api/v1/ratings/{ratingId}` - Delete rating

### Likes
- **POST** `/api/v1/likes` - Like content (comment/review/chapter)
- **DELETE** `/api/v1/likes/{likeId}` - Unlike content
- **GET** `/api/v1/likes/users/{userId}` - Get user's likes
- **GET** `/api/v1/likes/content/{contentId}` - Get likes for content

### Bookmarks
- **POST** `/api/v1/bookmarks` - Bookmark a novel/chapter
- **DELETE** `/api/v1/bookmarks/{bookmarkId}` - Remove bookmark
- **GET** `/api/v1/bookmarks/users/{userId}` - Get user's bookmarks
- **PUT** `/api/v1/bookmarks/{bookmarkId}` - Update bookmark (add notes)

### Reading Progress
- **POST** `/api/v1/reading-progress` - Update reading progress
- **GET** `/api/v1/reading-progress/users/{userId}/novel/{novelId}` - Get progress
- **GET** `/api/v1/reading-progress/users/{userId}` - Get all user progress
- **DELETE** `/api/v1/reading-progress/{progressId}` - Clear progress

### Follows
- **POST** `/api/v1/follows/users/{targetUserId}` - Follow a user
- **DELETE** `/api/v1/follows/users/{targetUserId}` - Unfollow a user
- **GET** `/api/v1/follows/users/{userId}/followers` - Get followers
- **GET** `/api/v1/follows/users/{userId}/following` - Get following
- **POST** `/api/v1/follows/novels/{novelId}` - Follow a novel
- **DELETE** `/api/v1/follows/novels/{novelId}` - Unfollow a novel

### Reading Lists
- **POST** `/api/v1/reading-lists` - Create reading list
- **GET** `/api/v1/reading-lists/{listId}` - Get reading list
- **PUT** `/api/v1/reading-lists/{listId}` - Update reading list
- **DELETE** `/api/v1/reading-lists/{listId}` - Delete reading list
- **POST** `/api/v1/reading-lists/{listId}/novels/{novelId}` - Add novel to list
- **DELETE** `/api/v1/reading-lists/{listId}/novels/{novelId}` - Remove novel from list
- **GET** `/api/v1/reading-lists/users/{userId}` - Get user's reading lists

### Notifications
- **GET** `/api/v1/notifications/users/{userId}` - Get user notifications
- **PUT** `/api/v1/notifications/{notificationId}/read` - Mark as read
- **PUT** `/api/v1/notifications/users/{userId}/read-all` - Mark all as read
- **DELETE** `/api/v1/notifications/{notificationId}` - Delete notification

### Recommendations
- **GET** `/api/v1/recommendations/users/{userId}` - Get personalized recommendations
- **GET** `/api/v1/recommendations/novels/{novelId}/similar` - Get similar novels
- **GET** `/api/v1/recommendations/trending` - Get trending content

---

## Key Features

### 💬 Comment System
- Nested comments (replies)
- Comment threads
- Comment editing and deletion
- Spoiler tags
- Comment moderation
- Report inappropriate comments
- Comment sorting (newest, oldest, most liked)

### ⭐ Review System
- Star ratings (1-5)
- Written reviews
- Review voting (helpful/not helpful)
- Review moderation
- Featured reviews
- Review templates

### 📊 Rating System
- Overall novel ratings
- Chapter-level ratings
- Aggregated rating statistics
- Rating distribution
- User rating history

### 👍 Like System
- Like comments
- Like reviews
- Like chapters
- Like notifications
- Unlike functionality

### 🔖 Bookmark System
- Bookmark novels
- Bookmark chapters
- Bookmark collections
- Personal notes on bookmarks
- Bookmark folders/tags

### 📖 Reading Progress Tracking
- Current chapter tracking
- Reading percentage
- Last read timestamp
- Continue reading feature
- Progress sync across devices

### 👥 Social Features
- Follow users
- Follow novels (get updates)
- Follower/following lists
- Activity feeds
- User mentions in comments

### 📚 Reading Lists
- Create custom reading lists
- Public/private lists
- Collaborative lists
- List sharing
- Curated collections

### 🔔 Notification System
- Real-time notifications
- Comment replies
- New chapter alerts
- Follow updates
- Like notifications
- Notification preferences

### 🎯 Recommendation Engine
- Personalized novel recommendations
- Similar novel suggestions
- Trending content
- Genre-based recommendations
- Collaborative filtering

---

## Database Schema

The Engagement Service uses the following key entities:

- **Comment** - User comments on novels/chapters
- **Review** - User reviews with ratings
- **Rating** - Numerical ratings for content
- **Like** - Likes on various content types
- **Bookmark** - Saved novels/chapters
- **ReadingProgress** - User reading progress
- **Follow** - User and novel follows
- **ReadingList** - Custom reading lists
- **Notification** - User notifications
- **Report** - Content reports for moderation

---

## Next Steps

Once this basic setup is working:
1. ✅ Create database entities (Comment, Review, Rating, etc.)
2. ✅ Set up Flyway migrations
3. ✅ Create repositories and services
4. ✅ Implement API endpoints
5. ✅ Add Feign clients for inter-service communication
6. ✅ Set up Redis caching for hot content
7. ✅ Implement WebSocket for real-time notifications
8. ✅ Add content moderation system
9. ✅ Implement recommendation algorithm
10. ✅ Set up Elasticsearch for search (optional)

---

## Troubleshooting

**Problem: Service won't register with Eureka**
- Ensure Eureka is running: `docker ps`
- Check logs: Look for "DiscoveryClient" messages
- Verify defaultZone URL is correct

**Problem: Port 8084 already in use**
- Find process: `lsof -i :8084` (Mac/Linux) or `netstat -ano | findstr :8084` (Windows)
- Kill process or change port in application.yml

**Problem: Database connection fails**
- Verify PostgreSQL is running: `docker ps | grep yushan-postgres`
- Check database credentials in application.yml
- Test connection: `psql -h localhost -U yushan_engagement -d yushan_engagement`

**Problem: Redis connection fails**
- Verify Redis is running: `docker ps | grep redis`
- Check Redis connection: `redis-cli ping`
- Verify Redis host and port in application.yml

**Problem: Build fails**
- Ensure Java 21 is installed: `java -version`
- Check Maven: `./mvnw -version`
- Clean and rebuild: `./mvnw clean install -U`

**Problem: Notifications not working**
- Check WebSocket connection logs
- Verify Redis pub/sub is working
- Review notification service logs
- Check user notification preferences

**Problem: Comments not loading**
- Check database indexes on foreign keys
- Verify caching is working properly
- Review pagination parameters
- Check for query performance issues

---

## Performance Tips
1. **Caching**: Cache popular content (hot comments, top reviews) in Redis
2. **Pagination**: Always use pagination for lists and feeds
3. **Indexing**: Index foreign keys, timestamps, and user_id columns
4. **Rate Limiting**: Limit comment/review creation frequency
5. **Async Processing**: Use async for notifications and analytics events
6. **Read Replicas**: Use database read replicas for heavy read operations

---

## Content Moderation
The Engagement Service includes moderation features:
- **Automated Filtering**: Profanity filter, spam detection
- **User Reports**: Allow users to report inappropriate content
- **Admin Tools**: Review and moderate reported content
- **Shadow Banning**: Soft-ban users for violations
- **Content Guidelines**: Enforce community guidelines

---

## Real-Time Features
Using WebSockets and Redis Pub/Sub:
- Live comment updates
- Real-time notifications
- Live reading progress sync
- Instant like updates
- Live follower counts

---

## Inter-Service Communication
The Engagement Service communicates with:
- **User Service**: Fetch user profiles and preferences
- **Content Service**: Validate novels and chapters exist
- **Gamification Service**: Trigger points for engagement activities
- **Analytics Service**: Send engagement metrics

---

## Security Considerations
- Validate content ownership before updates/deletes
- Implement rate limiting on write operations
- Sanitize user input to prevent XSS
- Use prepared statements to prevent SQL injection
- Implement CSRF protection
- Validate user permissions for private content
- Monitor for spam and abuse patterns

---

## Event Publishing
The Engagement Service publishes events for:
- Comment created/deleted
- Review posted
- Rating given
- Novel followed
- Reading progress updated

These events are consumed by Analytics and Gamification services.

---

## Monitoring
The Engagement Service exposes metrics through:
- Spring Boot Actuator endpoints (`/actuator/metrics`)
- Custom engagement metrics (comments/reviews per hour)
- Notification delivery success rate
- Cache hit rates
- WebSocket connection count

---

## License
This project is part of the Yushan Platform ecosystem.
