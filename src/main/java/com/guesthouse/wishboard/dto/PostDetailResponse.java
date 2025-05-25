package com.guesthouse.wishboard.dto;

import com.guesthouse.wishboard.entity.Community;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailResponse(
        Long communityId,        // ← postId → communityId
        String boardType,
        String communityType,
        String diversity,
        String title,
        String content,
        LocalDateTime createdAt,
        List<String> imageUrls
) {
    public static PostDetailResponse from(Community c, List<String> imageUrls) {
        return new PostDetailResponse(
                c.getCommunityId(),   // 여기 역시
                c.getType(),
                c.getCommunityType(),
                c.getCommunityDiversity(),
                c.getTitle(),
                c.getContent(),
                c.getCreatedAt(),
                imageUrls
        );
    }
}
