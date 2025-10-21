package com.yushan.engagement_service.client;

import com.yushan.engagement_service.config.FeignAuthConfig;
import com.yushan.engagement_service.dto.common.ApiResponse;
import com.yushan.engagement_service.dto.chapter.ChapterDetailResponseDTO;
import com.yushan.engagement_service.dto.novel.NovelDetailResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import feign.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "content-service", url = "${services.content.url:http://yushan-content-service:8082}", 
            configuration = FeignAuthConfig.class)
public interface ContentServiceClient {

    @PostMapping("/api/v1/chapters/batch/get")
    ApiResponse<List<ChapterDetailResponseDTO>> getChaptersBatch(@RequestBody List<Integer> chapterIds);

    @PostMapping("/api/v1/novels/batch/get")
    ApiResponse<List<NovelDetailResponseDTO>> getNovelsBatch(@RequestBody List<Integer> novelIds);

    @GetMapping("/api/v1/novels/{novelId}")
    ApiResponse<NovelDetailResponseDTO> getNovelById(@PathVariable("novelId") Integer novelId);

    @GetMapping("/api/v1/novels/{novelId}/vote-count")
    ApiResponse<Integer> getNovelVoteCount(@PathVariable("novelId") Integer novelId);

    @PostMapping("/api/v1/novels/{novelId}/vote")
    ApiResponse<String> incrementVoteCount(@PathVariable("novelId") Integer novelId);

    @PutMapping("/api/v1/novels/{novelId}/rating")
    ApiResponse<String> updateNovelRatingAndCount(
            @PathVariable("novelId") Integer novelId,
            @RequestParam("avgRating") Float avgRating,
            @RequestParam("reviewCount") Integer reviewCount);

    @GetMapping("/api/v1/chapters/novel/{novelId}")
    ApiResponse<com.yushan.engagement_service.dto.common.PageResponseDTO<ChapterDetailResponseDTO>> getChaptersByNovelId(@PathVariable("novelId") Integer novelId);

    // Raw variant to avoid deserialization issues when only existence is needed
    @GetMapping("/api/v1/novels/{novelId}")
    ApiResponse<Map<String, Object>> getNovelByIdRaw(@PathVariable("novelId") Integer novelId);

    // Low-level variant returning only HTTP response for existence checks
    @GetMapping("/api/v1/novels/{novelId}")
    Response headlessGetNovelById(@PathVariable("novelId") Integer novelId);

    default ChapterDetailResponseDTO getChapter(Integer chapterId) {
        try {
            List<Integer> chapterIds = List.of(chapterId);
            ApiResponse<List<ChapterDetailResponseDTO>> response = getChaptersBatch(chapterIds);
            
            if (response != null && response.getData() != null && !response.getData().isEmpty()) {
                return response.getData().get(0);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    default boolean chapterExists(Integer chapterId) {
        try {
            ChapterDetailResponseDTO chapter = getChapter(chapterId);
            return chapter != null && Boolean.TRUE.equals(chapter.getIsValid());
        } catch (Exception e) {
            return false;
        }
    }

    default List<Integer> getChapterIdsByNovelId(Integer novelId) {
        try {
            ApiResponse<com.yushan.engagement_service.dto.common.PageResponseDTO<ChapterDetailResponseDTO>> response = getChaptersByNovelId(novelId);
            if (response != null && response.getData() != null && response.getData().getContent() != null) {
                return response.getData().getContent().stream()
                        .map(ChapterDetailResponseDTO::getId)
                        .collect(java.util.stream.Collectors.toList());
            }
            return new java.util.ArrayList<>();
        } catch (Exception e) {
            System.out.println("Error getting chapters for novel " + novelId + ": " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
}