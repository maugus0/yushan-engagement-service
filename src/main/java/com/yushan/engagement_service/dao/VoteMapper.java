package com.yushan.engagement_service.dao;

import com.yushan.engagement_service.entity.Vote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface VoteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Vote record);

    int insertSelective(Vote record);

    Vote selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Vote record);

    int updateByPrimaryKey(Vote record);

    // Custom queries
    Vote selectByUserAndNovel(@Param("userId") UUID userId, @Param("novelId") Integer novelId);
    
    int deleteByUserAndNovel(@Param("userId") UUID userId, @Param("novelId") Integer novelId);

    long countByUserId(UUID userId);

    List<Vote> selectByUserIdWithPagination(UUID userId, int offset, int limit);
}