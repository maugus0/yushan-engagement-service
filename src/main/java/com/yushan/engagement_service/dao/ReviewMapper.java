package com.yushan.engagement_service.dao;

import com.yushan.engagement_service.dto.ReviewSearchRequestDTO;
import com.yushan.engagement_service.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface ReviewMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Review record);

    int insertSelective(Review record);

    Review selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Review record);

    int updateByPrimaryKey(Review record);

    Review selectByUuid(UUID uuid);

    Review selectByUserAndNovel(@Param("userId") UUID userId, @Param("novelId") Integer novelId);

    List<Review> selectByNovelId(@Param("novelId") Integer novelId);

    List<Review> selectByUserId(@Param("userId") UUID userId);

    List<Review> selectReviewsWithPagination(ReviewSearchRequestDTO request);

    long countReviews(ReviewSearchRequestDTO request);

    int updateLikeCount(@Param("id") Integer id, @Param("increment") int increment);
}