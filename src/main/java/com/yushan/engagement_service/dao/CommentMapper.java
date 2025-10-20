package com.yushan.engagement_service.dao;

import com.yushan.engagement_service.dto.comment.CommentSearchRequestDTO;
import com.yushan.engagement_service.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface CommentMapper {

    // Basic CRUD Operations
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    // Custom Queries
    List<Comment> selectByChapterId(@Param("chapterId") Integer chapterId);

    List<Comment> selectByUserId(@Param("userId") UUID userId);

    List<Comment> selectCommentsWithPagination(CommentSearchRequestDTO request);

    // Count Queries
    long countComments(CommentSearchRequestDTO request);

    long countByChapterId(@Param("chapterId") Integer chapterId);

    long countCommentsInLastDays(@Param("days") int days);

    long countCommentsByUser(@Param("userId") UUID userId);

    // Update Operations
    int updateLikeCount(@Param("id") Integer id, @Param("increment") int increment);

    int batchUpdateSpoilerStatus(@Param("ids") List<Integer> ids, @Param("isSpoiler") Boolean isSpoiler);

    // Check/Exists Queries
    boolean existsByUserAndChapter(@Param("userId") UUID userId, @Param("chapterId") Integer chapterId);

    // Admin/Moderation Queries
    Comment selectMostActiveUser();

    Comment selectMostCommentedChapter();

    int batchDeleteByIds(@Param("ids") List<Integer> ids);

    List<Comment> selectCommentsByKeyword(@Param("keyword") String keyword, @Param("limit") int limit);

    List<Comment> selectPopularComments(@Param("minLikes") int minLikes, @Param("limit") int limit);

    List<Comment> selectRecentComments(@Param("hours") int hours, @Param("limit") int limit);
}