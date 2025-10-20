package com.yushan.engagement_service.client;

import com.yushan.engagement_service.dto.ApiResponse;
import com.yushan.engagement_service.dto.ChapterDetailResponseDTO;
import com.yushan.engagement_service.dto.NovelDetailResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;

@Component
public class ContentServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.content.url:http://localhost:8082}")
    private String contentServiceUrl;

    public ContentServiceClient(RestTemplate restTemplate) {
        this.restTemplate = Objects.requireNonNull(restTemplate, "RestTemplate cannot be null");
    }

    public ChapterDetailResponseDTO getChapter(Integer chapterId) {
        try {
            String url = contentServiceUrl + "/api/v1/chapters/batch/get";
            List<Integer> chapterIds = List.of(chapterId);
            
            // Use ParameterizedTypeReference for proper type handling
            org.springframework.core.ParameterizedTypeReference<ApiResponse<List<ChapterDetailResponseDTO>>> responseType = 
                new org.springframework.core.ParameterizedTypeReference<ApiResponse<List<ChapterDetailResponseDTO>>>() {};
            
            org.springframework.http.HttpEntity<List<Integer>> requestEntity = new org.springframework.http.HttpEntity<>(chapterIds);
            ApiResponse<List<ChapterDetailResponseDTO>> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.POST, requestEntity, responseType).getBody();
            
            if (response != null && response.getData() != null && !response.getData().isEmpty()) {
                return response.getData().get(0);
            }
            return null;
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (Exception e) {
            // Log error
            throw new RuntimeException("Failed to fetch chapter from content service", e);
        }
    }

    public boolean chapterExists(Integer chapterId) {
        try {
            ChapterDetailResponseDTO chapter = getChapter(chapterId);
            return chapter != null && Boolean.TRUE.equals(chapter.getIsValid());
        } catch (Exception e) {
            return false;
        }
    }

    public NovelDetailResponseDTO getNovelById(Integer novelId) {
        try {
            String url = contentServiceUrl + "/api/v1/novels/" + novelId;
            
            // Use ParameterizedTypeReference for proper type handling
            org.springframework.core.ParameterizedTypeReference<ApiResponse<NovelDetailResponseDTO>> responseType = 
                new org.springframework.core.ParameterizedTypeReference<ApiResponse<NovelDetailResponseDTO>>() {};
            
            ApiResponse<NovelDetailResponseDTO> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, null, responseType).getBody();
            
            if (response != null && response.getData() != null) {
                return response.getData();
            }
            return null;
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (Exception e) {
            // Log error
            throw new RuntimeException("Failed to fetch novel from content service", e);
        }
    }

    public boolean novelExists(Integer novelId) {
        try {
            NovelDetailResponseDTO novel = getNovelById(novelId);
            return novel != null;
        } catch (Exception e) {
            return false;
        }
    }
}