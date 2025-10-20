package com.yushan.engagement_service.dao;

import com.yushan.engagement_service.dto.comment.CommentSearchRequestDTO;
import com.yushan.engagement_service.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface CommentMapper {
    // Basic CRUD operations
    int deleteByPrimaryKey(Integer id);
    int insert(Comment record);
    int insertSelective(Comment record);
    Comment selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(Comment record);
    int updateByPrimaryKey(Comment record);

    // Select by foreign keys
    List<Comment> selectByChapterId(Integer chapterId);
    List<Comment> selectByUserId(UUID userId);
    List<Comment> selectByNovelId(Integer novelId);

    // Paginated queries
    List<Comment> selectCommentsWithPagination(CommentSearchRequestDTO searchRequest);
    List<Comment> selectCommentsByNovelWithPagination(
            @Param("chapterIds") List<Integer> chapterIds,
            @Param("isSpoiler") Boolean isSpoiler,
            @Param("search") String search,
            @Param("sort") String sort,
            @Param("order") String order,
            @Param("page") int page,
            @Param("size") int size
    );

    // Count queries
    long countComments(CommentSearchRequestDTO searchRequest);
    long countByChapterId(Integer chapterId);
    long countByNovelId(List<Integer> chapterIds);
    long countCommentsByNovel(
            @Param("chapterIds") List<Integer> chapterIds,
            @Param("isSpoiler") Boolean isSpoiler,
            @Param("search") String search
    );

    // Like count update
    int updateLikeCount(@Param("id") Integer id, @Param("increment") Integer increment);

    // Validation/Check queries
    boolean existsByUserAndChapter(@Param("userId") UUID userId, @Param("chapterId") Integer chapterId);

    // Moderation queries
    long countCommentsInLastDays(@Param("days") int days);
    long countCommentsByUser(@Param("userId") UUID userId);
    Comment selectMostActiveUser();
    Comment selectMostCommentedChapter();
    int batchDeleteByIds(@Param("ids") List<Integer> ids);
    int batchUpdateSpoilerStatus(@Param("ids") List<Integer> ids, @Param("isSpoiler") Boolean isSpoiler);
    List<Comment> selectCommentsByKeyword(@Param("keyword") String keyword, @Param("limit") int limit);
    List<Comment> selectPopularComments(@Param("minLikes") int minLikes, @Param("limit") int limit);
    List<Comment> selectRecentComments(@Param("hours") int hours, @Param("limit") int limit);
}