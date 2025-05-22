package com.guesthouse.wishboard.dto;

import com.guesthouse.wishboard.entity.Community;

import java.time.LocalDateTime;

public record PostSummaryResponse(
        Long communityId,
        String boardType,
        String title,
        String diversity,
        LocalDateTime createdAt
) {
    public static PostSummaryResponse from(Community c) {
        return new PostSummaryResponse(
                c.getCommunityId(),   // PK 필드명 getCommunityId()
                c.getType(),
                c.getTitle(),
                c.getCommunityDiversity(),
                c.getCreatedAt()
        );
    }
}
