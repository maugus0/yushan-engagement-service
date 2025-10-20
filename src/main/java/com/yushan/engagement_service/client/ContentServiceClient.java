package com.yushan.engagement_service.client;

import com.yushan.engagement_service.dto.common.ApiResponse;
import com.yushan.engagement_service.dto.chapter.ChapterDetailResponseDTO;
import com.yushan.engagement_service.dto.novel.NovelDetailResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "content-service", url = "${services.content.url:http://localhost:8082}")
public interface ContentServiceClient {

    @PostMapping("/api/v1/chapters/batch/get")
    ApiResponse<List<ChapterDetailResponseDTO>> getChaptersBatch(@RequestBody List<Integer> chapterIds);

    @GetMapping("/api/v1/novels/{novelId}")
    ApiResponse<NovelDetailResponseDTO> getNovelById(@PathVariable("novelId") Integer novelId);

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

    default boolean novelExists(Integer novelId) {
        try {
            ApiResponse<NovelDetailResponseDTO> response = getNovelById(novelId);
            return response != null && response.getData() != null;
        } catch (Exception e) {
            return false;
        }
    }
}