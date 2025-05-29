package com.guesthouse.wishboard.dto;

import com.guesthouse.wishboard.entity.Community;

import java.time.LocalDateTime;
import java.util.List;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailResponse(
        Long postId,
        String type,
        String communityType,
        String diversity,
        String title,
        String content,
        String authorNickname,
        LocalDateTime createdAt,
        List<String> imageUrls,
        int likeCount,
        int commentCount
) {
    public static PostDetailResponse from(Community c, List<String> imageUrls) {
        return new PostDetailResponse(
                c.getCommunityId(),
                c.getType(),
                c.getCommunityType(),
                c.getCommunityDiversity(),
                c.getTitle(),
                c.getContent(),
                c.getUser().getNickname(),
                c.getCreatedAt(),
                imageUrls,
                c.getLikes() != null ? c.getLikes().size() : 0,
                c.getComments() != null ? c.getComments().size() : 0
        );
    }
}

