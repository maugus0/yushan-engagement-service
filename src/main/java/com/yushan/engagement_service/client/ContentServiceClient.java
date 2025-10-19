package com.yushan.engagement_service.client;

import com.yushan.engagement_service.dto.ChapterDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class ContentServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.content.url:http://localhost:8081}")
    private String contentServiceUrl;

    public ContentServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ChapterDTO getChapter(Integer chapterId) {
        try {
            String url = contentServiceUrl + "/api/chapters/" + chapterId;
            return restTemplate.getForObject(url, ChapterDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (Exception e) {
            // Log error
            throw new RuntimeException("Failed to fetch chapter from content service", e);
        }
    }

    public boolean chapterExists(Integer chapterId) {
        try {
            ChapterDTO chapter = getChapter(chapterId);
            return chapter != null && Boolean.TRUE.equals(chapter.getIsValid());
        } catch (Exception e) {
            return false;
        }
    }
}